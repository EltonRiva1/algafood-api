package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.CozinhaInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/cozinhas")
public class CozinhaController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CozinhaController.class);
	private final CozinhaRepository cozinhaRepository;
	private final CadastroCozinhaService cadastroCozinhaService;
	private final CozinhaModelAssembler cozinhaModelAssembler;
	private final CozinhaInputDisassembler cozinhaInputDisassembler;
	private final PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;

	public CozinhaController(CozinhaRepository cozinhaRepository, CadastroCozinhaService cadastroCozinhaService,
			CozinhaModelAssembler cozinhaModelAssembler, CozinhaInputDisassembler cozinhaInputDisassembler,
			PagedResourcesAssembler<Cozinha> pagedResourcesAssembler) {
		this.cozinhaRepository = cozinhaRepository;
		this.cadastroCozinhaService = cadastroCozinhaService;
		this.cozinhaModelAssembler = cozinhaModelAssembler;
		this.cozinhaInputDisassembler = cozinhaInputDisassembler;
		this.pagedResourcesAssembler = pagedResourcesAssembler;
	}

	@GetMapping
	@CheckSecurity.Cozinhas.PodeConsultar
	public ResponseEntity<PagedModel<?>> listar(@PageableDefault() Pageable pageable) {
		LOGGER.info("Consultando cozinhas com páginas de {} registros...", pageable.getPageSize());
		return ResponseEntity.ok(this.pagedResourcesAssembler.toModel(this.cozinhaRepository.findAll(pageable),
				this.cozinhaModelAssembler));
	}

	@GetMapping("/{cozinhaId}")
	@CheckSecurity.Cozinhas.PodeConsultar
	public ResponseEntity<?> buscar(@PathVariable Long cozinhaId) {
		return ResponseEntity
				.ok(this.cozinhaModelAssembler.toModel(this.cadastroCozinhaService.buscarOuFalhar(cozinhaId)));
	}

	@PostMapping
	@CheckSecurity.Cozinhas.PodeEditar
	public ResponseEntity<?> adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.cozinhaModelAssembler.toModel(
				this.cadastroCozinhaService.salvar(this.cozinhaInputDisassembler.toDomainObject(cozinhaInput))));
	}

	@PutMapping("/{cozinhaId}")
	@CheckSecurity.Cozinhas.PodeEditar
	public ResponseEntity<?> atualizar(@PathVariable Long cozinhaId, @RequestBody @Valid CozinhaInput cozinhaInput) {
		var cozinhaAtual = this.cadastroCozinhaService.buscarOuFalhar(cozinhaId);
		this.cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		return ResponseEntity.ok(this.cozinhaModelAssembler.toModel(this.cadastroCozinhaService.salvar(cozinhaAtual)));
	}

	@DeleteMapping("/{cozinhaId}")
	@CheckSecurity.Cozinhas.PodeEditar
	public ResponseEntity<?> remover(@PathVariable Long cozinhaId) {
		this.cadastroCozinhaService.excluir(cozinhaId);
		return ResponseEntity.noContent().build();
	}
}
