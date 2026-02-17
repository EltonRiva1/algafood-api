package com.algaworks.algafood.api.controller;

import java.util.List;
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

import com.algaworks.algafood.api.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cidades")
@Tag(name = "Cidades", description = "Gerencia as cidades")
public class CidadeController {
	private final CidadeRepository cidadeRepository;
	private final CadastroCidadeService cadastroCidadeService;
	private final CidadeModelAssembler cidadeModelAssembler;
	private final CidadeInputDisassembler cidadeInputDisassembler;

	public CidadeController(CidadeRepository cidadeRepository, CadastroCidadeService cadastroCidadeService,
			CidadeModelAssembler cidadeModelAssembler, CidadeInputDisassembler cidadeInputDisassembler) {
		this.cidadeRepository = cidadeRepository;
		this.cadastroCidadeService = cadastroCidadeService;
		this.cidadeModelAssembler = cidadeModelAssembler;
		this.cidadeInputDisassembler = cidadeInputDisassembler;
	}

	@GetMapping
	@Operation(summary = "Lista as cidades")
	public ResponseEntity<List<?>> listar() {
		return ResponseEntity.ok(this.cidadeModelAssembler.toCollectionModel(this.cidadeRepository.findAll()));
	}

	@GetMapping("/{cidadeId}")
	@Operation(summary = "Busca uma cidade por ID")
	public ResponseEntity<?> buscar(@PathVariable Long cidadeId) {
		return ResponseEntity
				.ok(this.cidadeModelAssembler.toModel(this.cadastroCidadeService.buscarOuFalhar(cidadeId)));
	}

	@PostMapping
	@Operation(summary = "Cadastra uma cidade")
	public ResponseEntity<?> adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.cidadeModelAssembler
				.toModel(this.cadastroCidadeService.salvar(this.cidadeInputDisassembler.toDomainObject(cidadeInput))));
	}

	@PutMapping("/{cidadeId}")
	@Operation(summary = "Atualiza uma cidade por ID")
	public ResponseEntity<?> atualizar(@PathVariable Long cidadeId, @RequestBody @Valid CidadeInput cidadeInput) {
		var cidadeAtual = this.cadastroCidadeService.buscarOuFalhar(cidadeId);
		this.cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
		return ResponseEntity.ok(this.cidadeModelAssembler.toModel(this.cadastroCidadeService.salvar(cidadeAtual)));
	}

	@DeleteMapping("/{cidadeId}")
	@Operation(summary = "Exclui uma cidade por ID")
	public ResponseEntity<?> remover(@PathVariable Long cidadeId) {
		this.cadastroCidadeService.excluir(cidadeId);
		return ResponseEntity.noContent().build();
	}
}
