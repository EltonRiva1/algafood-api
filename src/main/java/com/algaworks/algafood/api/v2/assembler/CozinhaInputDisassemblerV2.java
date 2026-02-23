package com.algaworks.algafood.api.v2.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v2.model.input.CozinhaInputV2;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaInputDisassemblerV2 {
	private final ModelMapper mapper;

	public CozinhaInputDisassemblerV2(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public Cozinha toDomainObject(CozinhaInputV2 cozinhaInputV2) {
		return this.mapper.map(cozinhaInputV2, Cozinha.class);
	}

	public void copyToDomainObject(CozinhaInputV2 cozinhaInputV2, Cozinha cozinha) {
		this.mapper.map(cozinhaInputV2, cozinha);
	}
}
