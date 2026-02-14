package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaInputDisassembler {
	private final ModelMapper mapper;

	public CozinhaInputDisassembler(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public Cozinha toDomainObject(CozinhaInput cozinhaInput) {
		return this.mapper.map(cozinhaInput, Cozinha.class);
	}

	public void copyToDomainObject(CozinhaInput cozinhaInput, Cozinha cozinha) {
		this.mapper.map(cozinhaInput, cozinha);
	}
}
