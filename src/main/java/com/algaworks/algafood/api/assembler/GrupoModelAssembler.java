package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.domain.model.Grupo;

@Component
public class GrupoModelAssembler {
    private final ModelMapper mapper;

    public GrupoModelAssembler(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public GrupoModel toModel(Grupo grupo) {
        return this.mapper.map(grupo, GrupoModel.class);
    }

    public List<?> toCollectionModel(Collection<Grupo> grupos) {
        return grupos.stream().map(this::toModel).collect(Collectors.toList());
    }
}
