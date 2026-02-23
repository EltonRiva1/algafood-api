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
import com.algaworks.algafood.api.v1.assembler.GrupoModelAssembler;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping(value = "/v1/usuarios/{usuarioId}/grupos")
public class UsuarioGrupoController {
	private final CadastroUsuarioService cadastroUsuarioService;
	private final GrupoModelAssembler grupoModelAssembler;
	private final AlgaLinks algaLinks;

	public UsuarioGrupoController(CadastroUsuarioService cadastroUsuarioService,
			GrupoModelAssembler grupoModelAssembler, AlgaLinks algaLinks) {
		this.cadastroUsuarioService = cadastroUsuarioService;
		this.grupoModelAssembler = grupoModelAssembler;
		this.algaLinks = algaLinks;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<?>> listar(@PathVariable Long usuarioId) {
		var gruposModel = this.grupoModelAssembler
				.toCollectionModel(this.cadastroUsuarioService.buscarOuFalhar(usuarioId).getGrupos()).removeLinks()
				.add(this.algaLinks.linkToUsuarioGrupoAssociacao(usuarioId, "associar"));
		gruposModel.getContent().forEach(grupoModel -> {
			grupoModel
					.add(this.algaLinks.linkToUsuarioGrupoDesassociacao(usuarioId, grupoModel.getId(), "desassociar"));
		});
		return ResponseEntity.ok(gruposModel);
	}

	@DeleteMapping("/{grupoId}")
	public ResponseEntity<?> desassociar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		this.cadastroUsuarioService.desassociarGrupo(usuarioId, grupoId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{grupoId}")
	public ResponseEntity<?> associar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		this.cadastroUsuarioService.associarGrupo(usuarioId, grupoId);
		return ResponseEntity.noContent().build();
	}
}
