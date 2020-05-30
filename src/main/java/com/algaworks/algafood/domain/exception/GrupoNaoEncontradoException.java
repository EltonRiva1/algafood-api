package com.algaworks.algafood.domain.exception;

public class GrupoNaoEncontradoException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GrupoNaoEncontradoException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}

	public GrupoNaoEncontradoException(Long grupoId) {
		// TODO Auto-generated constructor stub
		this(String.format("Não existe um cadastro de grupo com código %d", grupoId));
	}
}
