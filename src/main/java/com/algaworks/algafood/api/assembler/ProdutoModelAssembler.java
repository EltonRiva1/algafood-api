package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controller.RestauranteProdutoController;
import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.domain.model.Produto;

@Component
public class ProdutoModelAssembler extends RepresentationModelAssemblerSupport<Produto, ProdutoModel> {
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private AlgaLinks algaLinks;

	public ProdutoModelAssembler() {
		super(RestauranteProdutoController.class, ProdutoModel.class);
	}

	@Override
	public ProdutoModel toModel(Produto produto) {
		var produtoModel = createModelWithId(produto.getId(), produto, produto.getRestaurante().getId());
		mapper.map(produto, produtoModel);
		produtoModel.add(this.algaLinks.linkToProdutos(produto.getRestaurante().getId(), "produtos"));
		produtoModel.add(this.algaLinks.linkToFotoProduto(produto.getRestaurante().getId(), produto.getId(), "foto"));
		return produtoModel;
	}
}
