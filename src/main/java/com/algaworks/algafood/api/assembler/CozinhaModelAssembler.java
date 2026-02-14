package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaModelAssembler {
	private final ModelMapper mapper;

	public CozinhaModelAssembler(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public CozinhaModel toModel(Cozinha cozinha) {
		return this.mapper.map(cozinha, CozinhaModel.class);
	}

	public List<?> toCollectionModel(List<Cozinha> cozinhas) {
		return cozinhas.stream().map(this::toModel).collect(Collectors.toList());
	}
}
