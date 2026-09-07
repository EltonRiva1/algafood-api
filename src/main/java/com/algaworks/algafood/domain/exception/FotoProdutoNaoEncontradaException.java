package com.algaworks.algafood.domain.exception;

import java.io.Serial;

public class FotoProdutoNaoEncontradaException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	public FotoProdutoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public FotoProdutoNaoEncontradaException(Long restauranteId, Long produtoId) {
		this(String.format("Não existe um cadastro de foto do produto com código %d para o restaurante de código %d",
				produtoId, restauranteId));
	}
}
