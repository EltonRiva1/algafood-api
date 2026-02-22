package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private AlgaLinks algaLinks;

	public PedidoModelAssembler() {
		super(PedidoController.class, PedidoModel.class);
	}

	@Override
	public PedidoModel toModel(Pedido pedido) {
		var pedidoModel = this.createModelWithId(pedido.getId(), pedido);
		this.mapper.map(pedido, pedidoModel);
		pedidoModel.add(this.algaLinks.linkToPedidos("pedidos"));
		if (pedido.podeSerConfirmado())
			pedidoModel.add(this.algaLinks.linkToConfirmacaoPedido(pedido.getCodigo(), "confirmar"));
		if (pedido.podeSerCancelado())
			pedidoModel.add(this.algaLinks.linkToCancelamentoPedido(pedido.getCodigo(), "cancelar"));
		if (pedido.podeSerEntregue())
			pedidoModel.add(this.algaLinks.linkToEntregaPedido(pedido.getCodigo(), "entregar"));
		pedidoModel.getRestaurante().add(this.algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));
		pedidoModel.getCliente().add(this.algaLinks.linkToUsuario(pedido.getCliente().getId()));
		pedidoModel.getFormaPagamento().add(this.algaLinks.linkToFormaPagamento(pedido.getFormaPagamento().getId()));
		pedidoModel.getEnderecoEntrega().getCidade()
				.add(this.algaLinks.linkToCidade(pedido.getEnderecoEntrega().getCidade().getId()));
		pedidoModel.getItens().forEach(item -> {
			item.add(
					this.algaLinks.linkToProduto(pedidoModel.getRestaurante().getId(), item.getProdutoId(), "produto"));
		});
		return pedidoModel;
	}
}
