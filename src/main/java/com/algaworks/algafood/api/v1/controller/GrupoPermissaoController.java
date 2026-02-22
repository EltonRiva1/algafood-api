package com.algaworks.algafood.api.v1.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.PermissaoModelAssembler;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping("/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {
	private final CadastroGrupoService cadastroGrupoService;
	private final PermissaoModelAssembler permissaoModelAssembler;
	private final AlgaLinks algaLinks;

	public GrupoPermissaoController(CadastroGrupoService cadastroGrupoService,
			PermissaoModelAssembler permissaoModelAssembler, AlgaLinks algaLinks) {
		this.cadastroGrupoService = cadastroGrupoService;
		this.permissaoModelAssembler = permissaoModelAssembler;
		this.algaLinks = algaLinks;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<?>> listar(@PathVariable Long grupoId) {
		var permissoesModel = this.permissaoModelAssembler
				.toCollectionModel(this.cadastroGrupoService.buscarOuFalhar(grupoId).getPermissoes()).removeLinks()
				.add(this.algaLinks.linkToGrupoPermissoes(grupoId))
				.add(this.algaLinks.linkToGrupoPermissaoAssociacao(grupoId, "associar"));
		permissoesModel.getContent().forEach(permissaoModel -> {
			permissaoModel.add(
					this.algaLinks.linkToGrupoPermissaoDesassociacao(grupoId, permissaoModel.getId(), "desassociar"));
		});
		return ResponseEntity.ok(permissoesModel);
	}

	@DeleteMapping("/{permissaoId}")
	public ResponseEntity<?> desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		this.cadastroGrupoService.desassociarPermissao(grupoId, permissaoId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{permissaoId}")
	public ResponseEntity<?> associar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		this.cadastroGrupoService.associarPermissao(grupoId, permissaoId);
		return ResponseEntity.noContent().build();
	}
}
