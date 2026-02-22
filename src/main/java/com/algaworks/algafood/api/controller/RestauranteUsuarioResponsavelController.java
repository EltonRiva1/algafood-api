package com.algaworks.algafood.api.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/responsaveis")
public class RestauranteUsuarioResponsavelController {
	private final CadastroRestauranteService cadastroRestauranteService;
	private final UsuarioModelAssembler usuarioModelAssembler;
	private final AlgaLinks algaLinks;

	public RestauranteUsuarioResponsavelController(CadastroRestauranteService cadastroRestauranteService,
			UsuarioModelAssembler usuarioModelAssembler, AlgaLinks algaLinks) {
		this.cadastroRestauranteService = cadastroRestauranteService;
		this.usuarioModelAssembler = usuarioModelAssembler;
		this.algaLinks = algaLinks;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<?>> listar(@PathVariable Long restauranteId) {
		return ResponseEntity.ok(this.usuarioModelAssembler
				.toCollectionModel(
						this.cadastroRestauranteService.buscarOuFalharComResponsaveis(restauranteId).getResponsaveis())
				.removeLinks().add(this.algaLinks.linkToRestauranteResponsaveis(restauranteId)));
	}

	@DeleteMapping("/{usuarioId}")
	public ResponseEntity<?> desassociar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		this.cadastroRestauranteService.desassociarResponsavel(restauranteId, usuarioId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{usuarioId}")
	public ResponseEntity<?> associar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		this.cadastroRestauranteService.associarResponsavel(restauranteId, usuarioId);
		return ResponseEntity.noContent().build();
	}
}
