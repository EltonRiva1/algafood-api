package com.algaworks.algafood.api.model.input;

import jakarta.validation.constraints.NotBlank;

public class SenhaInput {
	@NotBlank
	private String senhaAtual, novaSenha;

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}
}
