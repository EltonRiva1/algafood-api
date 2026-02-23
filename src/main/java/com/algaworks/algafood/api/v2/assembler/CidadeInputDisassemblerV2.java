package com.algaworks.algafood.api.v2.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v2.model.input.CidadeInputV2;
import com.algaworks.algafood.domain.model.Cidade;

@Component
public class CidadeInputDisassemblerV2 {
	private final ModelMapper mapper;

	public CidadeInputDisassemblerV2(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public Cidade toDomainObject(CidadeInputV2 cidadeInputV2) {
		return this.mapper.map(cidadeInputV2, Cidade.class);
	}

	public void copyToDomainObject(CidadeInputV2 cidadeInputV2, Cidade cidade) {
		this.mapper.map(cidadeInputV2, cidade);
	}
}
