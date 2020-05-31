package com.algaworks.algafood.domain.exception;

public class FormaPagamentoNaoEncontradaException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FormaPagamentoNaoEncontradaException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}

	public FormaPagamentoNaoEncontradaException(Long formaPagamentoId) {
		// TODO Auto-generated constructor stub
		this(String.format("Não existe um cadastro de forma de pagamento com código %d", formaPagamentoId));
	}
}
