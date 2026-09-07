package com.algaworks.algafood.api.v1.controller;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.core.security.AlgaSecurity;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping("/v1")
public class RootEntryPointController {
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public RootEntryPointController(AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@GetMapping
	public RootEntryPointModel root() {
		var rootEntryPointModel = new RootEntryPointModel();
		if (algaSecurity.podeConsultarCozinhas())
			rootEntryPointModel.add(this.algaLinks.linkToCozinhas("cozinhas"));
		if (algaSecurity.podePesquisarPedidos())
			rootEntryPointModel.add(this.algaLinks.linkToPedidos("pedidos"));
		if (algaSecurity.podeConsultarRestaurantes())
			rootEntryPointModel.add(this.algaLinks.linkToRestaurantes("restaurantes"));
		if (algaSecurity.podeConsultarUsuariosGruposPermissoes())
			rootEntryPointModel.add(this.algaLinks.linkToGrupos("grupos"));
		rootEntryPointModel.add(this.algaLinks.linkToUsuarios("usuarios"));
		rootEntryPointModel.add(this.algaLinks.linkToPermissoes("permissoes"));
		if (algaSecurity.podeConsultarFormasPagamento())
			rootEntryPointModel.add(this.algaLinks.linkToFormasPagamento("formas-pagamento"));
		if (algaSecurity.podeConsultarEstados())
			rootEntryPointModel.add(this.algaLinks.linkToEstados("estados"));
		if (algaSecurity.podeConsultarCidades())
			rootEntryPointModel.add(this.algaLinks.linkToCidades("cidades"));
		if (algaSecurity.podeConsultarEstatisticas())
			rootEntryPointModel.add(this.algaLinks.linkToEstatisticas("estatisticas"));
		return rootEntryPointModel;
	}

	private static class RootEntryPointModel extends RepresentationModel<RootEntryPointModel> {
	}
}
