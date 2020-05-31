package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemPedidoModel {
	private Long produtoId;
	private String produtoNome, observacao;
	private Integer quantidade;
	private BigDecimal precoUnitario, precoTotal;
}
