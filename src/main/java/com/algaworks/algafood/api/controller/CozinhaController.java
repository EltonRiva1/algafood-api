package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.CozinhaInputDisassembler;
import com.algaworks.algafood.api.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

import jakarta.validation.Valid;

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
@RequestMapping(value = "/cozinhas")
public class CozinhaController {
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
	public ResponseEntity<PagedModel<?>> listar(@PageableDefault() Pageable pageable) {
		return ResponseEntity.ok(this.pagedResourcesAssembler.toModel(this.cozinhaRepository.findAll(pageable),
				this.cozinhaModelAssembler));
	}

	@GetMapping("/{cozinhaId}")
	public ResponseEntity<?> buscar(@PathVariable Long cozinhaId) {
		return ResponseEntity
				.ok(this.cozinhaModelAssembler.toModel(this.cadastroCozinhaService.buscarOuFalhar(cozinhaId)));
	}

	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.cozinhaModelAssembler.toModel(
				this.cadastroCozinhaService.salvar(this.cozinhaInputDisassembler.toDomainObject(cozinhaInput))));
	}

	@PutMapping("/{cozinhaId}")
	public ResponseEntity<?> atualizar(@PathVariable Long cozinhaId, @RequestBody @Valid CozinhaInput cozinhaInput) {
		var cozinhaAtual = this.cadastroCozinhaService.buscarOuFalhar(cozinhaId);
		this.cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		return ResponseEntity.ok(this.cozinhaModelAssembler.toModel(this.cadastroCozinhaService.salvar(cozinhaAtual)));
	}

	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<?> remover(@PathVariable Long cozinhaId) {
		this.cadastroCozinhaService.excluir(cozinhaId);
		return ResponseEntity.noContent().build();
	}
}
