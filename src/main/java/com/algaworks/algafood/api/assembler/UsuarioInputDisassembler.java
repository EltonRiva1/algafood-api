package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.domain.model.Usuario;

@Component
public class UsuarioInputDisassembler {
	private final ModelMapper mapper;

	public UsuarioInputDisassembler(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public Usuario toDomainObject(UsuarioInput usuarioInput) {
		return this.mapper.map(usuarioInput, Usuario.class);
	}

	public void copyToDomainObject(UsuarioInput usuarioInput, Usuario usuario) {
		this.mapper.map(usuarioInput, usuario);
	}
}
