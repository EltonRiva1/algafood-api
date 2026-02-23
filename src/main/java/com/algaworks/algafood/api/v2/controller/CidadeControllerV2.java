package com.algaworks.algafood.api.v2.controller;

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
import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.v2.assembler.CidadeInputDisassemblerV2;
import com.algaworks.algafood.api.v2.assembler.CidadeModelAssemblerV2;
import com.algaworks.algafood.api.v2.model.input.CidadeInputV2;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v2/cidades")
public class CidadeControllerV2 {
	private final CidadeRepository cidadeRepository;
	private final CadastroCidadeService cadastroCidadeService;
	private final CidadeModelAssemblerV2 cidadeModelAssemblerV2;
	private final CidadeInputDisassemblerV2 cidadeInputDisassemblerV2;

	public CidadeControllerV2(CidadeRepository cidadeRepository, CadastroCidadeService cadastroCidadeService,
			CidadeModelAssemblerV2 cidadeModelAssemblerV2, CidadeInputDisassemblerV2 cidadeInputDisassemblerV2) {
		this.cidadeRepository = cidadeRepository;
		this.cadastroCidadeService = cadastroCidadeService;
		this.cidadeModelAssemblerV2 = cidadeModelAssemblerV2;
		this.cidadeInputDisassemblerV2 = cidadeInputDisassemblerV2;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<?>> listar() {
		return ResponseEntity.ok(this.cidadeModelAssemblerV2.toCollectionModel(this.cidadeRepository.findAll()));
	}

	@GetMapping("/{cidadeId}")
	public ResponseEntity<?> buscar(@PathVariable Long cidadeId) {
		return ResponseEntity
				.ok(this.cidadeModelAssemblerV2.toModel(this.cadastroCidadeService.buscarOuFalhar(cidadeId)));
	}

	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody @Valid CidadeInputV2 cidadeInputV2) {
		var cidadeModel = this.cidadeModelAssemblerV2.toModel(
				this.cadastroCidadeService.salvar(this.cidadeInputDisassemblerV2.toDomainObject(cidadeInputV2)));
		ResourceUriHelper.addUriInResponseHeader(cidadeModel.getIdCidade());
		return ResponseEntity.status(HttpStatus.CREATED).body(cidadeModel);
	}

	@PutMapping("/{cidadeId}")
	public ResponseEntity<?> atualizar(@PathVariable Long cidadeId, @RequestBody @Valid CidadeInputV2 cidadeInputV2) {
		var cidadeAtual = this.cadastroCidadeService.buscarOuFalhar(cidadeId);
		this.cidadeInputDisassemblerV2.copyToDomainObject(cidadeInputV2, cidadeAtual);
		return ResponseEntity.ok(this.cidadeModelAssemblerV2.toModel(this.cadastroCidadeService.salvar(cidadeAtual)));
	}

	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<?> remover(@PathVariable Long cidadeId) {
		this.cadastroCidadeService.excluir(cidadeId);
		return ResponseEntity.noContent().build();
	}
}
