package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.model.Cidade;

@Component
public class CidadeInputDisassembler {
	private final ModelMapper mapper;

	public CidadeInputDisassembler(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public Cidade toDomainObject(CidadeInput cidadeInput) {
		return this.mapper.map(cidadeInput, Cidade.class);
	}

	public void copyToDomainObject(CidadeInput cidadeInput, Cidade cidade) {
		this.mapper.map(cidadeInput, cidade);
	}
}
