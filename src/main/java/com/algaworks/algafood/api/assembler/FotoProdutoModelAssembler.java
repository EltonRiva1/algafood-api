package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.FotoProdutoModel;
import com.algaworks.algafood.domain.model.FotoProduto;

@Component
public class FotoProdutoModelAssembler {
	private final ModelMapper mapper;

	public FotoProdutoModelAssembler(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public FotoProdutoModel toModel(FotoProduto fotoProduto) {
		return this.mapper.map(fotoProduto, FotoProdutoModel.class);
	}
}
