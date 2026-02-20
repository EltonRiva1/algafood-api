package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.CidadeController;
import com.algaworks.algafood.api.controller.FormaPagamentoController;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.controller.RestauranteProdutoController;
import com.algaworks.algafood.api.controller.UsuarioController;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {
	@Autowired
	private ModelMapper mapper;

	public PedidoModelAssembler() {
		super(PedidoController.class, PedidoModel.class);
	}

	@Override
	public PedidoModel toModel(Pedido pedido) {
		var pedidoModel = this.createModelWithId(pedido.getId(), pedido);
		this.mapper.map(pedido, pedidoModel);
		pedidoModel.add(WebMvcLinkBuilder.linkTo(PedidoController.class).withRel("pedidos"));
		pedidoModel.getRestaurante()
				.add(WebMvcLinkBuilder.linkTo(
						WebMvcLinkBuilder.methodOn(RestauranteController.class).buscar(pedido.getRestaurante().getId()))
						.withSelfRel());
		pedidoModel.getCliente()
				.add(WebMvcLinkBuilder
						.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class).buscar(pedido.getCliente().getId()))
						.withSelfRel());
		pedidoModel.getFormaPagamento()
				.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FormaPagamentoController.class)
						.buscar(pedido.getFormaPagamento().getId(), null)).withSelfRel());
		pedidoModel.getEnderecoEntrega().getCidade()
				.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CidadeController.class)
						.buscar(pedido.getEnderecoEntrega().getCidade().getId())).withSelfRel());
		pedidoModel.getItens().forEach(item -> {
			item.add(
					WebMvcLinkBuilder
							.linkTo(WebMvcLinkBuilder.methodOn(RestauranteProdutoController.class)
									.buscar(pedidoModel.getRestaurante().getId(), item.getProdutoId()))
							.withRel("produto"));
		});
		return pedidoModel;
	}
}
