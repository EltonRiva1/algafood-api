package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.domain.model.Permissao;

@Component
public class PermissaoModelAssembler {
    private final ModelMapper mapper;

    public PermissaoModelAssembler(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public PermissaoModel toModel(Permissao permissao) {
        return this.mapper.map(permissao, PermissaoModel.class);
    }

    public List<?> toCollectionModel(Collection<Permissao> permissoes) {
        return permissoes.stream().map(this::toModel).collect(Collectors.toList());
    }
}
