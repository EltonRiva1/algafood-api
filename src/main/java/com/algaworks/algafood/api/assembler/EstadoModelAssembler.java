package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.EstadoModel;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class EstadoModelAssembler {
	private final ModelMapper mapper;

	public EstadoModelAssembler(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public EstadoModel toModel(Estado estado) {
		return this.mapper.map(estado, EstadoModel.class);
	}

	public List<?> toCollectionModel(List<Estado> estados) {
		return estados.stream().map(this::toModel).collect(Collectors.toList());
	}
}
