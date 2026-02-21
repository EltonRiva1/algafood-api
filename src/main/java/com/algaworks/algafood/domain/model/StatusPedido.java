package com.algaworks.algafood.domain.model;

import java.util.Arrays;
import java.util.List;

public enum StatusPedido {
	CRIADO("Criado"), CONFIRMADO("Confirmado", CRIADO), ENTREGUE("Entregue", CONFIRMADO),
	CANCELADO("Cancelado", CRIADO);

	private final String descricao;
	private final List<StatusPedido> statusPedidos;

	StatusPedido(String descricao, StatusPedido... statusPedidos) {
		this.descricao = descricao;
		this.statusPedidos = Arrays.asList(statusPedidos);
	}

	public String getDescricao() {
		return descricao;
	}

	public boolean naoPodeAlterarPara(StatusPedido statusPedido) {
		return !statusPedido.statusPedidos.contains(this);
	}

	public boolean podeAlterarPara(StatusPedido statusPedido) {
		return !this.naoPodeAlterarPara(statusPedido);
	}
}
