package com.algaworks.algafood.core.security.authorizationserver;

import java.time.Duration;
import java.util.UUID;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class RegisteredClientInitializer implements ApplicationRunner {
	private final RegisteredClientRepository registeredClientRepository;
	private final PasswordEncoder passwordEncoder;

	public RegisteredClientInitializer(RegisteredClientRepository registeredClientRepository,
			PasswordEncoder passwordEncoder) {
		this.registeredClientRepository = registeredClientRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(ApplicationArguments args) {
		var tokenSettings = TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(30))
				.refreshTokenTimeToLive(Duration.ofDays(30)).reuseRefreshTokens(false).build();
		saveIfMissing(RegisteredClient.withId(UUID.randomUUID().toString()).clientId("algafood-web")
				.clientSecret(passwordEncoder.encode("web123"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri("https://oauth.pstmn.io/v1/callback").scope("READ").scope("WRITE")
				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
				.tokenSettings(tokenSettings).build());
		saveIfMissing(RegisteredClient.withId(UUID.randomUUID().toString()).clientId("foodanalytics")
				.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN).redirectUri("http://localhost:8082")
				.scope("READ").scope("WRITE")
				.clientSettings(
						ClientSettings.builder().requireProofKey(true).requireAuthorizationConsent(false).build())
				.tokenSettings(tokenSettings).build());
		saveIfMissing(
				RegisteredClient.withId(UUID.randomUUID().toString()).clientId("faturamento")
						.clientSecret(passwordEncoder.encode("faturamento123"))
						.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
						.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS).scope("READ").scope("WRITE")
						.clientSettings(ClientSettings.builder()
								.setting(JwtCustomClaimsTokenCustomizer.CLIENT_AUTHORITIES_SETTING,
										"CONSULTAR_PEDIDOS GERAR_RELATORIOS")
								.build())
						.tokenSettings(tokenSettings).build());
		saveIfMissing(RegisteredClient.withId(UUID.randomUUID().toString()).clientId("checktoken")
				.clientSecret(passwordEncoder.encode("check123"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS).scope("READ")
				.tokenSettings(tokenSettings).build());
	}

	private void saveIfMissing(RegisteredClient registeredClient) {
		if (registeredClientRepository.findByClientId(registeredClient.getClientId()) == null) {
			registeredClientRepository.save(registeredClient);
		}
	}
}
