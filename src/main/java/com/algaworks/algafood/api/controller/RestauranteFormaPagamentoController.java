package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/formas-pagamento")
public class RestauranteFormaPagamentoController {
	private final CadastroRestauranteService cadastroRestauranteService;
	private final FormaPagamentoModelAssembler formaPagamentoModelAssembler;

	public RestauranteFormaPagamentoController(CadastroRestauranteService cadastroRestauranteService,
			FormaPagamentoModelAssembler formaPagamentoModelAssembler) {
		this.cadastroRestauranteService = cadastroRestauranteService;
		this.formaPagamentoModelAssembler = formaPagamentoModelAssembler;
	}

	@GetMapping
	public ResponseEntity<List<?>> listar(@PathVariable Long restauranteId) {
		return ResponseEntity.ok(this.formaPagamentoModelAssembler.toCollectionModel(
				this.cadastroRestauranteService.buscarOuFalharComFormasPagamento(restauranteId).getFormasPagamento()));
	}

	@DeleteMapping("/{formaPagamentoId}")
	public ResponseEntity<?> desassociar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		this.cadastroRestauranteService.desassociarFormaPagamento(restauranteId, formaPagamentoId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{formaPagamentoId}")
	public ResponseEntity<?> associar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		this.cadastroRestauranteService.associarFormaPagamento(restauranteId, formaPagamentoId);
		return ResponseEntity.noContent().build();
	}
}
