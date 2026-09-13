package com.algaworks.algafood.core.security.authorizationserver;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oauth2/consents")
public class AuthorizedClientController {
	private final AuthorizedClientService authorizedClientService;

	public AuthorizedClientController(AuthorizedClientService authorizedClientService) {
		this.authorizedClientService = authorizedClientService;
	}

	@GetMapping
	public String list(Principal principal, Model model) {
		model.addAttribute("clients", authorizedClientService.findByPrincipalName(principal.getName()));
		return "pages/authorized-clients";
	}

	@PostMapping("/{clientId}/revoke")
	public String revoke(@PathVariable String clientId, Principal principal) {
		authorizedClientService.revoke(clientId, principal.getName());
		return "redirect:/oauth2/consents";
	}
}
