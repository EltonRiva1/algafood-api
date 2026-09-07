package com.algaworks.algafood.core.security.authorizationserver;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

@Configuration
public class JwtCustomClaimsTokenCustomizer {
	static final String CLIENT_AUTHORITIES_SETTING = "authorities";

	@Bean
	OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
		return context -> {
			if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()))
				return;
			Set<String> authorities = new HashSet<>();
			Object principal = context.getPrincipal().getPrincipal();
			if (principal instanceof AuthUser authUser) {
				authorities.addAll(authUser.getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.collect(Collectors.toSet()));
				context.getClaims().claim("usuario_id", authUser.getUsuarioId());
				context.getClaims().claim("nome_completo", authUser.getNomeCompleto());
			} else {
				authorities.addAll(getClientAuthorities(context));
			}
			context.getClaims().claim("authorities", authorities);
		};
	}

	private Collection<String> getClientAuthorities(JwtEncodingContext context) {
		Object authorities = context.getRegisteredClient().getClientSettings().getSetting(CLIENT_AUTHORITIES_SETTING);
		if (authorities instanceof String authoritiesAsString) {
			return Arrays.stream(authoritiesAsString.split("\\s+")).filter(authority -> !authority.isBlank())
					.collect(Collectors.toSet());
		}
		return Set.of();
	}
}
