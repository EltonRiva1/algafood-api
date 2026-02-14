package com.algaworks.algafood.api.model.input;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class RestauranteInput {
	@NotBlank
	private String nome;
	@NotNull
	@PositiveOrZero
	private BigDecimal taxaFrete;
	@NotNull
	@Valid
	private CozinhaIdInput cozinha;
	@Valid
	@NotNull
	private EnderecoInput endereco;

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

	public CozinhaIdInput getCozinha() {
		return cozinha;
	}

	public void setCozinha(CozinhaIdInput cozinha) {
		this.cozinha = cozinha;
	}

	public EnderecoInput getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoInput endereco) {
		this.endereco = endereco;
	}
}
