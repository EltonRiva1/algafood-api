package com.algaworks.algafood.api.v1.assembler;

import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.PedidoController;
import com.algaworks.algafood.api.v1.model.PedidoResumoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoResumoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoResumoModel> {
	private final ModelMapper mapper;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public PedidoResumoModelAssembler(ModelMapper mapper, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		super(PedidoController.class, PedidoResumoModel.class);
		this.mapper = mapper;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@Override
	@NonNull
	public PedidoResumoModel toModel(@NonNull Pedido pedido) {
		var pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
		this.mapper.map(pedido, pedidoModel);
		if (this.algaSecurity.podePesquisarPedidos())
			pedidoModel.add(this.algaLinks.linkToPedidos("pedidos"));
		if (this.algaSecurity.podeConsultarRestaurantes())
			pedidoModel.getRestaurante().add(this.algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));
		if (this.algaSecurity.podeConsultarUsuariosGruposPermissoes())
			pedidoModel.getCliente().add(this.algaLinks.linkToUsuario(pedido.getCliente().getId()));
		return pedidoModel;
	}
}
