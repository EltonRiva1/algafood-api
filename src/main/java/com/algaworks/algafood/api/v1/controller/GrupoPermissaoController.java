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
import com.algaworks.algafood.api.v1.assembler.PermissaoModelAssembler;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping("/v1/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {
	private final CadastroGrupoService cadastroGrupoService;
	private final PermissaoModelAssembler permissaoModelAssembler;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public GrupoPermissaoController(CadastroGrupoService cadastroGrupoService,
			PermissaoModelAssembler permissaoModelAssembler, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		this.cadastroGrupoService = cadastroGrupoService;
		this.permissaoModelAssembler = permissaoModelAssembler;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@GetMapping
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	public ResponseEntity<CollectionModel<?>> listar(@PathVariable Long grupoId) {
		var permissoesModel = this.permissaoModelAssembler
				.toCollectionModel(this.cadastroGrupoService.buscarOuFalhar(grupoId).getPermissoes()).removeLinks();
		permissoesModel.add(this.algaLinks.linkToGrupoPermissoes(grupoId));
		if (this.algaSecurity.podeEditarUsuariosGruposPermissoes())
			permissoesModel.add(this.algaLinks.linkToGrupoPermissaoAssociacao(grupoId, "associar")).getContent()
					.forEach(permissaoModel -> permissaoModel.add(this.algaLinks
							.linkToGrupoPermissaoDesassociacao(grupoId, permissaoModel.getId(), "desassociar")));
		return ResponseEntity.ok(permissoesModel);
	}

	@DeleteMapping("/{permissaoId}")
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	public ResponseEntity<?> desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		this.cadastroGrupoService.desassociarPermissao(grupoId, permissaoId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{permissaoId}")
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	public ResponseEntity<?> associar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		this.cadastroGrupoService.associarPermissao(grupoId, permissaoId);
		return ResponseEntity.noContent().build();
	}
}
