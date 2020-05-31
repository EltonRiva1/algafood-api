package com.algaworks.algafood.domain.exception;

public class RestauranteNaoEncontradoException extends EntidadeNaoEncontradaException {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	public RestauranteNaoEncontradoException(String mensagem) {
		// TODO Auto-generated constructor stub
		super(mensagem);
	}

	public RestauranteNaoEncontradoException(Long restauranteId) {
		// TODO Auto-generated constructor stub
		this(String.format("Não existe um cadastro de restaurante com código %d", restauranteId));
	}
}
