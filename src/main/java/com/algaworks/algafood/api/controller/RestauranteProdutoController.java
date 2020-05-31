package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.ProdutoInputDisassembler;
import com.algaworks.algafood.api.assembler.ProdutoModelAssembler;
import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.api.model.input.ProdutoInput;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController {
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private CadastroProdutoService cadastroProdutoService;
	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	@Autowired
	private ProdutoModelAssembler produtoModelAssembler;
	@Autowired
	private ProdutoInputDisassembler produtoInputDisassembler;

	@GetMapping
	public List<ProdutoModel> listar(@PathVariable Long restauranteId) {
		Restaurante restaurante = this.cadastroRestauranteService.buscarOuFalhar(restauranteId);
		List<Produto> todosProdutos = this.produtoRepository.findByRestaurante(restaurante);
		return this.produtoModelAssembler.toCollectionModel(todosProdutos);
	}

	@GetMapping("/{produtoId}")
	public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		Produto produto = this.cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);
		return this.produtoModelAssembler.toModel(produto);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoModel adicionar(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoInput produtoInput) {
		Restaurante restaurante = this.cadastroRestauranteService.buscarOuFalhar(restauranteId);
		Produto produto = this.produtoInputDisassembler.toDomainObject(produtoInput);
		produto.setRestaurante(restaurante);
		produto = this.cadastroProdutoService.salvar(produto);
		return this.produtoModelAssembler.toModel(produto);
	}

	@PutMapping("/{produtoId}")
	public ProdutoModel atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId,
			@RequestBody @Valid ProdutoInput produtoInput) {
		Produto produtoAtual = this.cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);
		this.produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);
		produtoAtual = this.cadastroProdutoService.salvar(produtoAtual);
		return this.produtoModelAssembler.toModel(produtoAtual);
	}
}
