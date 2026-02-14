package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoInputDisassembler {
	private final ModelMapper mapper;

	public PedidoInputDisassembler(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public Pedido toDomainObject(PedidoInput pedidoInput) {
		return this.mapper.map(pedidoInput, Pedido.class);
	}

	public void copyToDomainObject(PedidoInput pedidoInput, Pedido pedido) {
		this.mapper.map(pedidoInput, pedido);
	}
}
