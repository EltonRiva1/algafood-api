package com.algaworks.algafood.domain.exception;

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	public CozinhaNaoEncontradaException(String mensagem) {
		// TODO Auto-generated constructor stub
		super(mensagem);
	}

	public CozinhaNaoEncontradaException(Long cozinhaId) {
		// TODO Auto-generated constructor stub
		this(String.format("Não existe um cadastro de cozinha com código %d", cozinhaId));
	}
}
