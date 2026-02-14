package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.model.Grupo;

@Component
public class GrupoInputDisassembler {
	private final ModelMapper mapper;

	public GrupoInputDisassembler(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public Grupo toDomainObject(GrupoInput grupoInput) {
		return this.mapper.map(grupoInput, Grupo.class);
	}

	public void copyToDomainObject(GrupoInput grupoInput, Grupo grupo) {
		this.mapper.map(grupoInput, grupo);
	}
}
