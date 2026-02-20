package com.algaworks.algafood.api.controller;

import org.springframework.hateoas.CollectionModel;
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

import com.algaworks.algafood.api.assembler.EstadoInputDisassembler;
import com.algaworks.algafood.api.assembler.EstadoModelAssembler;
import com.algaworks.algafood.api.model.input.EstadoInput;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/estados")
public class EstadoController {
	private final EstadoRepository estadoRepository;
	private final CadastroEstadoService cadastroEstadoService;
	private final EstadoModelAssembler estadoModelAssembler;
	private final EstadoInputDisassembler estadoInputDisassembler;

	public EstadoController(EstadoRepository estadoRepository, CadastroEstadoService cadastroEstadoService,
			EstadoModelAssembler estadoModelAssembler, EstadoInputDisassembler estadoInputDisassembler) {
		this.estadoRepository = estadoRepository;
		this.cadastroEstadoService = cadastroEstadoService;
		this.estadoModelAssembler = estadoModelAssembler;
		this.estadoInputDisassembler = estadoInputDisassembler;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<?>> listar() {
		return ResponseEntity.ok(this.estadoModelAssembler.toCollectionModel(this.estadoRepository.findAll()));
	}

	@GetMapping("/{estadoId}")
	public ResponseEntity<?> buscar(@PathVariable Long estadoId) {
		return ResponseEntity
				.ok(this.estadoModelAssembler.toModel(this.cadastroEstadoService.buscarOuFalhar(estadoId)));
	}

	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody @Valid EstadoInput estadoInput) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.estadoModelAssembler
				.toModel(this.cadastroEstadoService.salvar(this.estadoInputDisassembler.toDomainObject(estadoInput))));
	}

	@PutMapping("/{estadoId}")
	public ResponseEntity<?> atualizar(@PathVariable Long estadoId, @RequestBody @Valid EstadoInput estadoInput) {
		var estadoAtual = this.cadastroEstadoService.buscarOuFalhar(estadoId);
		this.estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);
		return ResponseEntity.ok(this.estadoModelAssembler.toModel(this.cadastroEstadoService.salvar(estadoAtual)));
	}

	@DeleteMapping("/{estadoId}")
	public ResponseEntity<?> remover(@PathVariable Long estadoId) {
		this.cadastroEstadoService.excluir(estadoId);
		return ResponseEntity.noContent().build();
	}
}
