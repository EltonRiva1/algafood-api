package com.algaworks.algafood.core.security.authorizationserver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "algafood.jwt.keystore")
public record JwtKeyStoreProperties(Resource jksLocation, String password, String keypairAlias) {
}
