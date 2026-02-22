package com.algaworks.algafood.api.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PermissaoModelAssembler;
import com.algaworks.algafood.domain.repository.PermissaoRepository;

@RestController
@RequestMapping(path = "/permissoes")
public class PermissaoController {
	private final PermissaoRepository permissaoRepository;
	private final PermissaoModelAssembler permissaoModelAssembler;

	public PermissaoController(PermissaoRepository permissaoRepository,
			PermissaoModelAssembler permissaoModelAssembler) {
		this.permissaoRepository = permissaoRepository;
		this.permissaoModelAssembler = permissaoModelAssembler;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<?>> listar() {
		return ResponseEntity.ok(this.permissaoModelAssembler.toCollectionModel(this.permissaoRepository.findAll()));
	}
}
