package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.GrupoModelAssembler;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping(value = "/usuarios/{usuarioId}/grupos")
public class UsuarioGrupoController {
	private final CadastroUsuarioService cadastroUsuarioService;
	private final GrupoModelAssembler grupoModelAssembler;

	public UsuarioGrupoController(CadastroUsuarioService cadastroUsuarioService,
			GrupoModelAssembler grupoModelAssembler) {
		this.cadastroUsuarioService = cadastroUsuarioService;
		this.grupoModelAssembler = grupoModelAssembler;
	}

	@GetMapping
	public ResponseEntity<List<?>> listar(@PathVariable Long usuarioId) {
		return ResponseEntity.ok(this.grupoModelAssembler
				.toCollectionModel(this.cadastroUsuarioService.buscarComGruposOuFalhar(usuarioId).getGrupos()));
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
