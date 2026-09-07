package com.algaworks.algafood.domain.exception;

import java.io.Serial;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	public PedidoNaoEncontradoException(String codigo) {
		super(String.format("Não existe um pedido com código %s", codigo));
	}
}
