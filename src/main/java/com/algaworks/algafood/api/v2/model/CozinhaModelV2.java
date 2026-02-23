package com.algaworks.algafood.api.v2.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "cozinhas")
public class CozinhaModelV2 extends RepresentationModel<CozinhaModelV2> {
	private Long idCozinha;
	private String nomeCozinha;

	public Long getIdCozinha() {
		return idCozinha;
	}

	public void setIdCozinha(Long idCozinha) {
		this.idCozinha = idCozinha;
	}

	public String getNomeCozinha() {
		return nomeCozinha;
	}

	public void setNomeCozinha(String nomeCozinha) {
		this.nomeCozinha = nomeCozinha;
	}
}
