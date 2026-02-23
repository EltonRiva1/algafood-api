package com.algaworks.algafood.api.v2.model.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CidadeInputV2 {
	@NotBlank
	private String nomeCidade;
	@NotNull
	private Long idEstado;

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}
}
