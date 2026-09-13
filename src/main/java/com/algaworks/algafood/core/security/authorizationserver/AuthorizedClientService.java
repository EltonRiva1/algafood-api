package com.algaworks.algafood.core.security.authorizationserver;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorizedClientService {
	private final JdbcTemplate jdbcTemplate;
	private final RegisteredClientRepository registeredClientRepository;
	private final OAuth2AuthorizationConsentService authorizationConsentService;

	public AuthorizedClientService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository,
			OAuth2AuthorizationConsentService authorizationConsentService) {
		this.jdbcTemplate = jdbcTemplate;
		this.registeredClientRepository = registeredClientRepository;
		this.authorizationConsentService = authorizationConsentService;
	}

	public List<AuthorizedClient> findByPrincipalName(String principalName) {
		var sql = """
				select
				    rc.client_id,
				    rc.client_name,
				    c.authorities
				from oauth2_authorization_consent c
				join oauth2_registered_client rc
				    on rc.id = c.registered_client_id
				where c.principal_name = ?
				order by rc.client_name
				""";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new AuthorizedClient(rs.getString("client_id"),
				rs.getString("client_name"), parseScopes(rs.getString("authorities"))), principalName);
	}

	@Transactional
	public void revoke(String clientId, String principalName) {
		var registeredClient = registeredClientRepository.findByClientId(clientId);
		if (registeredClient == null) {
			return;
		}
		var consent = authorizationConsentService.findById(registeredClient.getId(), principalName);
		if (consent != null) {
			authorizationConsentService.remove(consent);
		}
		jdbcTemplate.update("""
				delete from oauth2_authorization
				where registered_client_id = ?
				  and principal_name = ?
				""", registeredClient.getId(), principalName);
	}

	private Set<String> parseScopes(String authorities) {
		if (authorities == null || authorities.isBlank()) {
			return Set.of();
		}
		return Arrays.stream(authorities.split(",")).map(String::trim).filter(value -> !value.isBlank())
				.map(value -> value.startsWith("SCOPE_") ? value.substring("SCOPE_".length()) : value)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}
}
