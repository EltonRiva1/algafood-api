package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.ProdutoInputDisassembler;
import com.algaworks.algafood.api.assembler.ProdutoModelAssembler;
import com.algaworks.algafood.api.model.input.ProdutoInput;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController {
	private final ProdutoRepository produtoRepository;
	private final CadastroProdutoService cadastroProdutoService;
	private final CadastroRestauranteService cadastroRestauranteService;
	private final ProdutoModelAssembler produtoModelAssembler;
	private final ProdutoInputDisassembler produtoInputDisassembler;

	public RestauranteProdutoController(ProdutoRepository produtoRepository,
			CadastroProdutoService cadastroProdutoService, CadastroRestauranteService cadastroRestauranteService,
			ProdutoModelAssembler produtoModelAssembler, ProdutoInputDisassembler produtoInputDisassembler) {
		this.produtoRepository = produtoRepository;
		this.cadastroProdutoService = cadastroProdutoService;
		this.cadastroRestauranteService = cadastroRestauranteService;
		this.produtoModelAssembler = produtoModelAssembler;
		this.produtoInputDisassembler = produtoInputDisassembler;
	}

	@GetMapping
	public ResponseEntity<List<?>> listar(@PathVariable Long restauranteId,
			@RequestParam(required = false, defaultValue = "false") boolean incluirInativos) {
		return ResponseEntity.ok(this.produtoModelAssembler.toCollectionModel(incluirInativos
				? this.produtoRepository
						.findTodosByRestaurante(this.cadastroRestauranteService.buscarOuFalhar(restauranteId))
				: this.produtoRepository
						.findAtivosByRestaurante(this.cadastroRestauranteService.buscarOuFalhar(restauranteId))));
	}

	@GetMapping("/{produtoId}")
	public ResponseEntity<?> buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		return ResponseEntity.ok(this.produtoModelAssembler
				.toModel(this.cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId)));
	}

	@PostMapping
	public ResponseEntity<?> adicionar(@PathVariable Long restauranteId,
			@RequestBody @Valid ProdutoInput produtoInput) {
		var produto = this.produtoInputDisassembler.toDomainObject(produtoInput);
		produto.setRestaurante(this.cadastroRestauranteService.buscarOuFalhar(restauranteId));
		produto = this.cadastroProdutoService.salvar(produto);
		return ResponseEntity.status(HttpStatus.CREATED).body(this.produtoModelAssembler.toModel(produto));
	}

	@PutMapping("/{produtoId}")
	public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId,
			@RequestBody @Valid ProdutoInput produtoInput) {
		var produtoAtual = this.cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);
		this.produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);
		produtoAtual = this.cadastroProdutoService.salvar(produtoAtual);
		return ResponseEntity.ok(this.produtoModelAssembler.toModel(produtoAtual));
	}
}
