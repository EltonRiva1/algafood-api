package com.algaworks.algafood.api.v2.controller;

import com.algaworks.algafood.api.v2.assembler.CozinhaInputDisassemblerV2;
import com.algaworks.algafood.api.v2.assembler.CozinhaModelAssemblerV2;
import com.algaworks.algafood.api.v2.model.input.CozinhaInputV2;
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
@RequestMapping(value = "/v2/cozinhas")
public class CozinhaControllerV2 {
	private final CozinhaRepository cozinhaRepository;
	private final CadastroCozinhaService cadastroCozinhaService;
	private final CozinhaModelAssemblerV2 cozinhaModelAssemblerV2;
	private final CozinhaInputDisassemblerV2 cozinhaInputDisassemblerV2;
	private final PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;

	public CozinhaControllerV2(CozinhaRepository cozinhaRepository, CadastroCozinhaService cadastroCozinhaService,
			CozinhaModelAssemblerV2 cozinhaModelAssemblerV2, CozinhaInputDisassemblerV2 cozinhaInputDisassemblerV2,
			PagedResourcesAssembler<Cozinha> pagedResourcesAssembler) {
		this.cozinhaRepository = cozinhaRepository;
		this.cadastroCozinhaService = cadastroCozinhaService;
		this.cozinhaModelAssemblerV2 = cozinhaModelAssemblerV2;
		this.cozinhaInputDisassemblerV2 = cozinhaInputDisassemblerV2;
		this.pagedResourcesAssembler = pagedResourcesAssembler;
	}

	@GetMapping
	public ResponseEntity<PagedModel<?>> listar(@PageableDefault() Pageable pageable) {
		return ResponseEntity.ok(this.pagedResourcesAssembler.toModel(this.cozinhaRepository.findAll(pageable),
				this.cozinhaModelAssemblerV2));
	}

	@GetMapping("/{cozinhaId}")
	public ResponseEntity<?> buscar(@PathVariable Long cozinhaId) {
		return ResponseEntity
				.ok(this.cozinhaModelAssemblerV2.toModel(this.cadastroCozinhaService.buscarOuFalhar(cozinhaId)));
	}

	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody @Valid CozinhaInputV2 cozinhaInputV2) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.cozinhaModelAssemblerV2.toModel(
				this.cadastroCozinhaService.salvar(this.cozinhaInputDisassemblerV2.toDomainObject(cozinhaInputV2))));
	}

	@PutMapping("/{cozinhaId}")
	public ResponseEntity<?> atualizar(@PathVariable Long cozinhaId,
			@RequestBody @Valid CozinhaInputV2 cozinhaInputV2) {
		var cozinhaAtual = this.cadastroCozinhaService.buscarOuFalhar(cozinhaId);
		this.cozinhaInputDisassemblerV2.copyToDomainObject(cozinhaInputV2, cozinhaAtual);
		return ResponseEntity
				.ok(this.cozinhaModelAssemblerV2.toModel(this.cadastroCozinhaService.salvar(cozinhaAtual)));
	}

	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<?> remover(@PathVariable Long cozinhaId) {
		this.cadastroCozinhaService.excluir(cozinhaId);
		return ResponseEntity.noContent().build();
	}
}
