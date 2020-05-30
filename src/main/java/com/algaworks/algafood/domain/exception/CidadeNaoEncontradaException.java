package com.algaworks.algafood.domain.exception;

public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaException {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	public CidadeNaoEncontradaException(String mensagem) {
		// TODO Auto-generated constructor stub
		super(mensagem);
	}

	public CidadeNaoEncontradaException(Long cidadeId) {
		// TODO Auto-generated constructor stub
		this(String.format("Não existe um cadastro de cidade com código %d", cidadeId));
	}
}
