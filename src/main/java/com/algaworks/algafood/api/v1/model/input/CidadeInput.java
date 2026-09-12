package com.algaworks.algafood.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CidadeInput {
	@NotBlank
	@Schema(example = "Uberlândia")
	private String nome;
	@Valid
	@NotNull
	private EstadoIdInput estado;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public EstadoIdInput getEstado() {
		return estado;
	}

	public void setEstado(EstadoIdInput estado) {
		this.estado = estado;
	}
}
