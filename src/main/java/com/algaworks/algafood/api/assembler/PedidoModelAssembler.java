package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoModelAssembler {
    private final ModelMapper mapper;

    public PedidoModelAssembler(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public PedidoModel toModel(Pedido pedido) {
        return this.mapper.map(pedido, PedidoModel.class);
    }

    public List<?> toCollectionModel(Collection<Pedido> pedidos) {
        return pedidos.stream().map(this::toModel).collect(Collectors.toList());
    }
}
