package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.EstadoInput;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class EstadoInputDisassembler {
	private final ModelMapper mapper;

	public EstadoInputDisassembler(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public Estado toDomainObject(EstadoInput estadoInput) {
		return this.mapper.map(estadoInput, Estado.class);
	}

	public void copyToDomainObject(EstadoInput estadoInput, Estado estado) {
		this.mapper.map(estadoInput, estado);
	}
}
