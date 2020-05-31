package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoInputDisassembler {
	@Autowired
	private ModelMapper mapper;

	public Pedido toDomainObject(PedidoInput input) {
		return this.mapper.map(input, Pedido.class);
	}

	public void copyToDomainObject(PedidoInput input, Pedido pedido) {
		this.mapper.map(input, pedido);
	}
}
