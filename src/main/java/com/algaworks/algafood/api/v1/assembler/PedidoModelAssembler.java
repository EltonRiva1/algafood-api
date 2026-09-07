package com.algaworks.algafood.api.v1.assembler;

import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.PedidoController;
import com.algaworks.algafood.api.v1.model.PedidoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {
	private final ModelMapper mapper;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public PedidoModelAssembler(ModelMapper mapper, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		super(PedidoController.class, PedidoModel.class);
		this.mapper = mapper;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@Override
	@NonNull
	public PedidoModel toModel(@NonNull Pedido pedido) {
		var pedidoModel = this.createModelWithId(pedido.getId(), pedido);
		this.mapper.map(pedido, pedidoModel);
		if (this.algaSecurity.podePesquisarPedidos())
			pedidoModel.add(this.algaLinks.linkToPedidos("pedidos"));
		if (this.algaSecurity.podeGerenciarPedidos(pedido.getCodigo())) {
			if (pedido.podeSerConfirmado())
				pedidoModel.add(this.algaLinks.linkToConfirmacaoPedido(pedido.getCodigo(), "confirmar"));
			if (pedido.podeSerCancelado())
				pedidoModel.add(this.algaLinks.linkToCancelamentoPedido(pedido.getCodigo(), "cancelar"));
			if (pedido.podeSerEntregue())
				pedidoModel.add(this.algaLinks.linkToEntregaPedido(pedido.getCodigo(), "entregar"));
		}
		if (this.algaSecurity.podeConsultarRestaurantes())
			pedidoModel.getRestaurante().add(this.algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));
		if (this.algaSecurity.podeConsultarUsuariosGruposPermissoes())
			pedidoModel.getCliente().add(this.algaLinks.linkToUsuario(pedido.getCliente().getId()));
		if (this.algaSecurity.podeConsultarFormasPagamento())
			pedidoModel.getFormaPagamento()
					.add(this.algaLinks.linkToFormaPagamento(pedido.getFormaPagamento().getId()));
		if (this.algaSecurity.podeConsultarCidades())
			pedidoModel.getEnderecoEntrega().getCidade()
					.add(this.algaLinks.linkToCidade(pedido.getEnderecoEntrega().getCidade().getId()));
		if (this.algaSecurity.podeConsultarRestaurantes()) {
			pedidoModel.getItens().forEach(item -> item.add(this.algaLinks
					.linkToProduto(pedidoModel.getRestaurante().getId(), item.getProdutoId(), "produto")));
		}
		return pedidoModel;
	}
}
