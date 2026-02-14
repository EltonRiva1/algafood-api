package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.ProdutoInput;
import com.algaworks.algafood.domain.model.Produto;

@Component
public class ProdutoInputDisassembler {
	private final ModelMapper mapper;

	public ProdutoInputDisassembler(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public Produto toDomainObject(ProdutoInput produtoInput) {
		return this.mapper.map(produtoInput, Produto.class);
	}

	public void copyToDomainObject(ProdutoInput produtoInput, Produto produto) {
		this.mapper.map(produtoInput, produto);
	}
}
