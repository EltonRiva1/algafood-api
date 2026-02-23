package com.algaworks.algafood.api.v2;

import com.algaworks.algafood.api.v2.controller.CidadeControllerV2;

import org.springframework.hateoas.*;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class AlgaLinksV2 {
	public Link linkToCidades(String rel) {
		return WebMvcLinkBuilder.linkTo(CidadeControllerV2.class).withRel(rel);
	}

	public Link linkToCidades() {
		return this.linkToCidades(IanaLinkRelations.SELF.value());
	}
}
