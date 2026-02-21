package com.algaworks.algafood.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "pedidos")
public class PedidoResumoModel extends RepresentationModel<PedidoResumoModel> {
	private String codigo;
	private BigDecimal subtotal, taxaFrete, valorTotal;
	private String status;
	private OffsetDateTime dataCriacao;
	private RestauranteApenasNomeModel restaurante;
	private UsuarioModel cliente;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getTaxaFrete() {
		return taxaFrete;
	}

	public void setTaxaFrete(BigDecimal taxaFrete) {
		this.taxaFrete = taxaFrete;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public OffsetDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(OffsetDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public RestauranteApenasNomeModel getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(RestauranteApenasNomeModel restaurante) {
		this.restaurante = restaurante;
	}

	public UsuarioModel getCliente() {
		return cliente;
	}

	public void setCliente(UsuarioModel cliente) {
		this.cliente = cliente;
	}
}
