package com.algaworks.algafood.core.security.authorizationserver;

import java.security.Principal;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizationConsentController {
	private final RegisteredClientRepository registeredClientRepository;
	private final OAuth2AuthorizationConsentService authorizationConsentService;

	public AuthorizationConsentController(RegisteredClientRepository registeredClientRepository,
			OAuth2AuthorizationConsentService authorizationConsentService) {
		this.registeredClientRepository = registeredClientRepository;
		this.authorizationConsentService = authorizationConsentService;
	}

	@GetMapping("/oauth2/consent")
	public String consent(Principal principal, Model model,
			@RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
			@RequestParam(name = OAuth2ParameterNames.SCOPE, defaultValue = "") String scope,
			@RequestParam(OAuth2ParameterNames.STATE) String state) {
		var registeredClient = registeredClientRepository.findByClientId(clientId);
		if (registeredClient == null) {
			throw new IllegalArgumentException("Cliente OAuth2 não encontrado: " + clientId);
		}
		OAuth2AuthorizationConsent consent = authorizationConsentService.findById(registeredClient.getId(),
				principal.getName());
		Set<String> previouslyApprovedScopes = consent == null ? Collections.emptySet() : consent.getScopes();
		Set<String> scopesToApprove = new LinkedHashSet<>();
		for (String requestedScope : StringUtils.delimitedListToStringArray(scope, " ")) {
			if (!requestedScope.isBlank() && !previouslyApprovedScopes.contains(requestedScope)) {
				scopesToApprove.add(requestedScope);
			}
		}
		model.addAttribute("clientId", clientId);
		model.addAttribute("clientName", registeredClient.getClientName());
		model.addAttribute("state", state);
		model.addAttribute("scopes", scopesToApprove);
		model.addAttribute("previouslyApprovedScopes", previouslyApprovedScopes);
		return "pages/consent";
	}
}
