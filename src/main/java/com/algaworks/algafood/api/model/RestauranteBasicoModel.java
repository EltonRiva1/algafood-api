package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "restaurantes")
public class RestauranteBasicoModel extends RepresentationModel<RestauranteBasicoModel> {
	private Long id;
	private String nome;
	private BigDecimal taxaFrete;
	private CozinhaModel cozinha;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getTaxaFrete() {
		return taxaFrete;
	}

	public void setTaxaFrete(BigDecimal taxaFrete) {
		this.taxaFrete = taxaFrete;
	}

	public CozinhaModel getCozinha() {
		return cozinha;
	}

	public void setCozinha(CozinhaModel cozinha) {
		this.cozinha = cozinha;
	}
}
