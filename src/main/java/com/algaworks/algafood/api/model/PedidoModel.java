package com.algaworks.algafood.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PedidoModel {
	private String codigo;
	private BigDecimal subtotal, taxaFrete, valorTotal;
	private String status;
	private OffsetDateTime dataCriacao, dataConfirmacao, dataEntrega, dataCancelamento;
	private RestauranteResumoModel restaurante;
	private UsuarioModel cliente;
	private FormaPagamentoModel formaPagamento;
	private EnderecoModel enderecoEntrega;
	private List<ItemPedidoModel> itens;
}
