package com.algaworks.algafood.core.security.authorizationserver;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("algafood.auth-server")
public record AuthorizationServerProperties(String issuer) {
}
