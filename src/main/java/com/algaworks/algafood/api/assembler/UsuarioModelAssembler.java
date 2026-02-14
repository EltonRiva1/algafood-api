package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.domain.model.Usuario;

@Component
public class UsuarioModelAssembler {
    private final ModelMapper mapper;

    public UsuarioModelAssembler(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public UsuarioModel toModel(Usuario usuario) {
        return this.mapper.map(usuario, UsuarioModel.class);
    }

    public List<?> toCollectionModel(Collection<Usuario> usuarios) {
        return usuarios.stream().map(this::toModel).collect(Collectors.toList());
    }
}
