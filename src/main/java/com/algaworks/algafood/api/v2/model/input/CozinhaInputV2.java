package com.algaworks.algafood.api.v2.model.input;

import jakarta.validation.constraints.NotBlank;

public class CozinhaInputV2 {
	@NotBlank
	private String nomeCozinha;

	public String getNomeCozinha() {
		return nomeCozinha;
	}

	public void setNomeCozinha(String nomeCozinha) {
		this.nomeCozinha = nomeCozinha;
	}
}
