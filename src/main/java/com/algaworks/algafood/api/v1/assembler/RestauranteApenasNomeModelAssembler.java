package com.algaworks.algafood.api.v1.assembler;

import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.api.v1.model.RestauranteApenasNomeModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteApenasNomeModelAssembler
		extends RepresentationModelAssemblerSupport<Restaurante, RestauranteApenasNomeModel> {
	private final ModelMapper modelMapper;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public RestauranteApenasNomeModelAssembler(ModelMapper modelMapper, AlgaLinks algaLinks,
			AlgaSecurity algaSecurity) {
		super(RestauranteController.class, RestauranteApenasNomeModel.class);
		this.modelMapper = modelMapper;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@Override
	@NonNull
	public RestauranteApenasNomeModel toModel(@NonNull Restaurante restaurante) {
		var restauranteModel = createModelWithId(restaurante.getId(), restaurante);
		this.modelMapper.map(restaurante, restauranteModel);
		if (this.algaSecurity.podeConsultarRestaurantes())
			restauranteModel.add(this.algaLinks.linkToRestaurantes("restaurantes"));
		return restauranteModel;
	}

	@Override
	@NonNull
	public CollectionModel<RestauranteApenasNomeModel> toCollectionModel(
			@NonNull Iterable<? extends Restaurante> entities) {
		var collectionModel = super.toCollectionModel(entities);
		if (this.algaSecurity.podeConsultarRestaurantes())
			collectionModel.add(this.algaLinks.linkToRestaurantes());
		return collectionModel;
	}
}
