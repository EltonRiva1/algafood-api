package com.algaworks.algafood.api.model.input;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PedidoInput {
	@Valid
	@NotNull
	private RestauranteIdInput restaurante;
	@Valid
	@NotNull
	private EnderecoInput enderecoEntrega;
	@Valid
	@NotNull
	private FormaPagamentoIdInput formaPagamento;
	@Valid
	@Size(min = 1)
	@NotNull
	private List<ItemPedidoInput> itens;

	public RestauranteIdInput getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(RestauranteIdInput restaurante) {
		this.restaurante = restaurante;
	}

	public EnderecoInput getEnderecoEntrega() {
		return enderecoEntrega;
	}

	public void setEnderecoEntrega(EnderecoInput enderecoEntrega) {
		this.enderecoEntrega = enderecoEntrega;
	}

	public FormaPagamentoIdInput getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamentoIdInput formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public List<ItemPedidoInput> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedidoInput> itens) {
		this.itens = itens;
	}
}
