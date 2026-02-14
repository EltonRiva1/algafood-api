package com.algaworks.algafood.api.model.input;

import jakarta.validation.constraints.NotNull;

public class CidadeIdInput {
	@NotNull
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
