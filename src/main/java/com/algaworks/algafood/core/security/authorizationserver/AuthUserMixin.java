package com.algaworks.algafood.core.security.authorizationserver;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(value = { "enabled", "accountNonExpired", "accountNonLocked",
		"credentialsNonExpired" }, ignoreUnknown = true)
public abstract class AuthUserMixin {
	@JsonCreator
	public AuthUserMixin(@JsonProperty("usuarioId") long usuarioId, @JsonProperty("nomeCompleto") String nomeCompleto,
			@JsonProperty("username") String username, @JsonProperty("password") String password,
			@JsonProperty("authorities") List<GrantedAuthority> authorities) {
	}
}
