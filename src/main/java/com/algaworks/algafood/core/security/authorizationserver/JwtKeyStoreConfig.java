package com.algaworks.algafood.core.security.authorizationserver;

import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class JwtKeyStoreConfig {

	@Bean
	JWKSource<SecurityContext> jwkSource(JwtKeyStoreProperties properties) {
		try {
			Resource resource = properties.jksLocation();
			KeyStore keyStore = KeyStore.getInstance("JKS");
			try (var inputStream = resource.getInputStream()) {
				keyStore.load(inputStream, properties.password().toCharArray());
			}
			Key key = keyStore.getKey(properties.keypairAlias(), properties.password().toCharArray());
			Certificate certificate = keyStore.getCertificate(properties.keypairAlias());
			RSAPublicKey publicKey = (RSAPublicKey) certificate.getPublicKey();
			RSAPrivateKey privateKey = (RSAPrivateKey) key;
			RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(properties.keypairAlias())
					.build();
			JWKSet jwkSet = new JWKSet(rsaKey);
			return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
		} catch (Exception e) {
			throw new IllegalStateException("Erro ao carregar o keystore JWT", e);
		}
	}
}
