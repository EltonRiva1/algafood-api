package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteModelAssembler {
	private final ModelMapper modelMapper;

	public RestauranteModelAssembler(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public RestauranteModel toModel(Restaurante restaurante) {
		return this.modelMapper.map(restaurante, RestauranteModel.class);
	}

	public List<?> toCollectionModel(List<Restaurante> restaurantes) {
		return restaurantes.stream().map(this::toModel).collect(Collectors.toList());
	}
}
