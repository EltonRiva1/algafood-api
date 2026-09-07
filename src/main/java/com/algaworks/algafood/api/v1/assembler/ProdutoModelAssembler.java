package com.algaworks.algafood.api.v1.assembler;

import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteProdutoController;
import com.algaworks.algafood.api.v1.model.ProdutoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Produto;

@Component
public class ProdutoModelAssembler extends RepresentationModelAssemblerSupport<Produto, ProdutoModel> {
	private final ModelMapper mapper;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public ProdutoModelAssembler(ModelMapper mapper, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		super(RestauranteProdutoController.class, ProdutoModel.class);
		this.mapper = mapper;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@Override
	@NonNull
	public ProdutoModel toModel(@NonNull Produto produto) {
		var produtoModel = createModelWithId(produto.getId(), produto, produto.getRestaurante().getId());
		this.mapper.map(produto, produtoModel);
		if (this.algaSecurity.podeConsultarRestaurantes())
			produtoModel.add(this.algaLinks.linkToProdutos(produto.getRestaurante().getId(), "produtos"))
					.add(this.algaLinks.linkToFotoProduto(produto.getRestaurante().getId(), produto.getId(), "foto"));
		return produtoModel;
	}
}