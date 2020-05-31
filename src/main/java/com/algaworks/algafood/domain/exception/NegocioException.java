package com.algaworks.algafood.domain.exception;

public class NegocioException extends RuntimeException {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	public NegocioException(String mensagem) {
		// TODO Auto-generated constructor stub
		super(mensagem);
	}

	public NegocioException(String mensagem, Throwable throwable) {
		// TODO Auto-generated constructor stub
		super(mensagem, throwable);
	}
}
