package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PermissaoModelAssembler;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping("/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {
	private final CadastroGrupoService cadastroGrupoService;
	private final PermissaoModelAssembler permissaoModelAssembler;

	public GrupoPermissaoController(CadastroGrupoService cadastroGrupoService,
			PermissaoModelAssembler permissaoModelAssembler) {
		this.cadastroGrupoService = cadastroGrupoService;
		this.permissaoModelAssembler = permissaoModelAssembler;
	}

	@GetMapping
	public ResponseEntity<List<?>> listar(@PathVariable Long grupoId) {
		return ResponseEntity.ok(this.permissaoModelAssembler
				.toCollectionModel(this.cadastroGrupoService.buscarComPermissoesOuFalhar(grupoId).getPermissoes()));
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
