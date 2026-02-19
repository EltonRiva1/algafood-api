package com.algaworks.algafood.api;

import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public final class ResourceUriHelper {
	private ResourceUriHelper() {
		throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada");
	}

	public static void addUriInResponseHeader(Object resourceId) {
		var uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(resourceId).toUri();
		var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes != null && attributes.getResponse() != null)
			attributes.getResponse().setHeader(HttpHeaders.LOCATION, uri.toString());
	}
}
