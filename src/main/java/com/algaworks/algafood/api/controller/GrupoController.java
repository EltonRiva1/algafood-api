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

import com.algaworks.algafood.api.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.api.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/grupos")
public class GrupoController {
	private final GrupoRepository grupoRepository;
	private final CadastroGrupoService cadastroGrupoService;
	private final GrupoModelAssembler grupoModelAssembler;
	private final GrupoInputDisassembler grupoInputDisassembler;

	public GrupoController(GrupoRepository grupoRepository, CadastroGrupoService cadastroGrupoService,
			GrupoModelAssembler grupoModelAssembler, GrupoInputDisassembler grupoInputDisassembler) {
		this.grupoRepository = grupoRepository;
		this.cadastroGrupoService = cadastroGrupoService;
		this.grupoModelAssembler = grupoModelAssembler;
		this.grupoInputDisassembler = grupoInputDisassembler;
	}

	@GetMapping
	public ResponseEntity<List<?>> listar() {
		return ResponseEntity.ok(this.grupoModelAssembler.toCollectionModel(this.grupoRepository.findAll()));
	}

	@GetMapping("/{grupoId}")
	public ResponseEntity<?> buscar(@PathVariable Long grupoId) {
		return ResponseEntity.ok(this.grupoModelAssembler.toModel(this.cadastroGrupoService.buscarOuFalhar(grupoId)));
	}

	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody @Valid GrupoInput grupoInput) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.grupoModelAssembler
				.toModel(this.cadastroGrupoService.salvar(this.grupoInputDisassembler.toDomainObject(grupoInput))));
	}

	@PutMapping("/{grupoId}")
	public ResponseEntity<?> atualizar(@PathVariable Long grupoId, @RequestBody @Valid GrupoInput grupoInput) {
		var grupoAtual = this.cadastroGrupoService.buscarOuFalhar(grupoId);
		this.grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);
		return ResponseEntity.ok(this.grupoModelAssembler.toModel(this.cadastroGrupoService.salvar(grupoAtual)));
	}

	@DeleteMapping("/{grupoId}")
	public ResponseEntity<?> remover(@PathVariable Long grupoId) {
		this.cadastroGrupoService.excluir(grupoId);
		return ResponseEntity.noContent().build();
	}
}
