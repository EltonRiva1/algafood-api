package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoResumoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoResumoModel> {
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private AlgaLinks algaLinks;

	public PedidoResumoModelAssembler() {
		super(PedidoController.class, PedidoResumoModel.class);
	}

	@Override
	public PedidoResumoModel toModel(Pedido pedido) {
		var pedidoModel = this.createModelWithId(pedido.getCodigo(), pedido);
		this.mapper.map(pedido, pedidoModel);
		pedidoModel.add(this.algaLinks.linkToPedidos("pedidos"));
		pedidoModel.getRestaurante().add(this.algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));
		pedidoModel.getCliente().add(this.algaLinks.linkToUsuario(pedido.getCliente().getId()));
		return pedidoModel;
	}
}
