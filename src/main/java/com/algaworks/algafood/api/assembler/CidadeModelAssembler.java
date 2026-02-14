package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.domain.model.Cidade;

@Component
public class CidadeModelAssembler {
	private final ModelMapper mapper;

	public CidadeModelAssembler(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public CidadeModel toModel(Cidade cidade) {
		return this.mapper.map(cidade, CidadeModel.class);
	}

	public List<?> toCollectionModel(List<Cidade> cidades) {
		return cidades.stream().map(this::toModel).collect(Collectors.toList());
	}
}
