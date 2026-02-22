package com.algaworks.algafood.api.controller;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.AlgaLinks;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping
public class RootEntryPointController {
	private final AlgaLinks algaLinks;

	public RootEntryPointController(AlgaLinks algaLinks) {
		this.algaLinks = algaLinks;
	}

	@GetMapping
	public RootEntryPointModel root() {
		var rootEntryPointModel = new RootEntryPointModel();
		rootEntryPointModel.add(this.algaLinks.linkToCozinhas("cozinhas"));
		rootEntryPointModel.add(this.algaLinks.linkToPedidos("pedidos"));
		rootEntryPointModel.add(this.algaLinks.linkToRestaurantes("restaurantes"));
		rootEntryPointModel.add(this.algaLinks.linkToGrupos("grupos"));
		rootEntryPointModel.add(this.algaLinks.linkToUsuarios("usuarios"));
		rootEntryPointModel.add(this.algaLinks.linkToPermissoes("permissoes"));
		rootEntryPointModel.add(this.algaLinks.linkToFormasPagamento("formas-pagamento"));
		rootEntryPointModel.add(this.algaLinks.linkToEstados("estados"));
		rootEntryPointModel.add(this.algaLinks.linkToCidades("cidades"));
		rootEntryPointModel.add(this.algaLinks.linkToEstatisticas("estatisticas"));
		return rootEntryPointModel;
	}

	private static class RootEntryPointModel extends RepresentationModel<RootEntryPointModel> {
	}
}
