package com.algaworks.algafood.api.v1.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.PermissaoModelAssembler;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.repository.PermissaoRepository;

@RestController
@RequestMapping(path = "/v1/permissoes")
public class PermissaoController {
	private final PermissaoRepository permissaoRepository;
	private final PermissaoModelAssembler permissaoModelAssembler;

	public PermissaoController(PermissaoRepository permissaoRepository,
			PermissaoModelAssembler permissaoModelAssembler) {
		this.permissaoRepository = permissaoRepository;
		this.permissaoModelAssembler = permissaoModelAssembler;
	}

	@GetMapping
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	public ResponseEntity<CollectionModel<?>> listar() {
		return ResponseEntity.ok(this.permissaoModelAssembler.toCollectionModel(this.permissaoRepository.findAll()));
	}
}
