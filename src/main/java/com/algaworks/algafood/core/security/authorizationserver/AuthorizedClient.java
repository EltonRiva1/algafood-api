package com.algaworks.algafood.core.security.authorizationserver;

import java.util.Set;

public record AuthorizedClient(String clientId, String clientName, Set<String> scopes) {
}
