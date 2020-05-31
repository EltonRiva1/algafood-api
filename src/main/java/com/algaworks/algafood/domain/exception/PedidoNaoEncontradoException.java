package com.algaworks.algafood.domain.exception;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PedidoNaoEncontradoException(String codigoPedido) {
		// TODO Auto-generated constructor stub
		super(String.format("Não existe um pedido de código %s", codigoPedido));
	}
}
