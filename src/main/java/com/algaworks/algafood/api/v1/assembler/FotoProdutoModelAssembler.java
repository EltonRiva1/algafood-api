package com.algaworks.algafood.api.v1.assembler;

import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteProdutoFotoController;
import com.algaworks.algafood.api.v1.model.FotoProdutoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.FotoProduto;

@Component
public class FotoProdutoModelAssembler extends RepresentationModelAssemblerSupport<FotoProduto, FotoProdutoModel> {
	private final ModelMapper mapper;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public FotoProdutoModelAssembler(ModelMapper mapper, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		super(RestauranteProdutoFotoController.class, FotoProdutoModel.class);
		this.mapper = mapper;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@Override
	@NonNull
	public FotoProdutoModel toModel(@NonNull FotoProduto foto) {
		var fotoProdutoModel = this.mapper.map(foto, FotoProdutoModel.class);
		if (this.algaSecurity.podeConsultarRestaurantes())
			fotoProdutoModel.add(this.algaLinks.linkToFotoProduto(foto.getRestauranteId(), foto.getProduto().getId()))
					.add(this.algaLinks.linkToProduto(foto.getRestauranteId(), foto.getProduto().getId(), "produto"));
		return fotoProdutoModel;
	}
}
