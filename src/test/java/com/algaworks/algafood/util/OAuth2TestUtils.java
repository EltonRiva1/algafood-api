package com.algaworks.algafood.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

public final class OAuth2TestUtils {

	private OAuth2TestUtils() {
	}

	public static String gerarToken(JwtEncoder jwtEncoder, List<String> scopes, String... authorities) {
		Instant agora = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder().issuedAt(agora).expiresAt(agora.plus(30, ChronoUnit.MINUTES))
				.subject("usuario-teste@algafood.com").claim("scope", scopes)
				.claim("authorities", Arrays.asList(authorities)).build();
		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
}
