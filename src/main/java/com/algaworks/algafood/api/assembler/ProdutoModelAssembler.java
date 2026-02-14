package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.domain.model.Produto;

@Component
public class ProdutoModelAssembler {
    private final ModelMapper mapper;

    public ProdutoModelAssembler(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ProdutoModel toModel(Produto produto) {
        return this.mapper.map(produto, ProdutoModel.class);
    }

    public List<?> toCollectionModel(List<Produto> produtos) {
        return produtos.stream().map(this::toModel).collect(Collectors.toList());
    }
}
