package com.algaworks.algafood.core.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ResourceServerConfig {
	@Bean
	@Order(2)
	SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
		return http.securityMatcher("/v1", "/v1/**", "/v2", "/v2/**").csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.GET, "/v1").permitAll()
						.requestMatchers(HttpMethod.GET, "/v1/restaurantes/*/produtos/*/foto").permitAll().anyRequest()
						.authenticated())
				.oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
				.build();
	}

	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
		return jwtAuthenticationConverter;
	}

	private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
		var scopesAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		return jwt -> {
			var scopeAuthorities = scopesAuthoritiesConverter.convert(jwt);
			var authorities = new ArrayList<>(scopeAuthorities);
			var tokenAuthorities = jwt.getClaimAsStringList("authorities");
			if (tokenAuthorities != null) {
				tokenAuthorities.stream().map(SimpleGrantedAuthority::new).forEach(authorities::add);
			}
			return authorities;
		};
	}
}