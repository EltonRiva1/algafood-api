package com.algaworks.algafood.api.v1.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping("/v1/restaurantes/{restauranteId}/responsaveis")
public class RestauranteUsuarioResponsavelController {
	private final CadastroRestauranteService cadastroRestauranteService;
	private final UsuarioModelAssembler usuarioModelAssembler;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public RestauranteUsuarioResponsavelController(CadastroRestauranteService cadastroRestauranteService,
			UsuarioModelAssembler usuarioModelAssembler, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		this.cadastroRestauranteService = cadastroRestauranteService;
		this.usuarioModelAssembler = usuarioModelAssembler;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@GetMapping
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	public ResponseEntity<CollectionModel<?>> listar(@PathVariable Long restauranteId) {
		var usuariosModel = this.usuarioModelAssembler
				.toCollectionModel(this.cadastroRestauranteService.buscarOuFalhar(restauranteId).getResponsaveis())
				.removeLinks().add(algaLinks.linkToRestauranteResponsaveis(restauranteId));
		if (algaSecurity.podeGerenciarCadastroRestaurantes())
			usuariosModel.add(algaLinks.linkToRestauranteResponsavelAssociacao(restauranteId, "associar")).getContent()
					.forEach(usuarioModel -> usuarioModel.add(algaLinks.linkToRestauranteResponsavelDesassociacao(
							restauranteId, usuarioModel.getId(), "desassociar")));
		return ResponseEntity.ok(usuariosModel);
	}

	@DeleteMapping("/{usuarioId}")
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	public ResponseEntity<?> desassociar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		this.cadastroRestauranteService.desassociarResponsavel(restauranteId, usuarioId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{usuarioId}")
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	public ResponseEntity<?> associar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		this.cadastroRestauranteService.associarResponsavel(restauranteId, usuarioId);
		return ResponseEntity.noContent().build();
	}
}
