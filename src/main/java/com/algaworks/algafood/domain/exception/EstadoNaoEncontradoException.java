package com.algaworks.algafood.domain.exception;

public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	public EstadoNaoEncontradoException(String mensagem) {
		// TODO Auto-generated constructor stub
		super(mensagem);
	}

	public EstadoNaoEncontradoException(Long estadoId) {
		// TODO Auto-generated constructor stub
		this(String.format("Não existe um cadastro de estado com código %d", estadoId));
	}
}
