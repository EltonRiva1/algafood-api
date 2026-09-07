package com.algaworks.algafood.api.v1.assembler;

import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteBasicoModelAssembler
		extends RepresentationModelAssemblerSupport<Restaurante, RestauranteBasicoModel> {
	private final ModelMapper modelMapper;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public RestauranteBasicoModelAssembler(ModelMapper modelMapper, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		super(RestauranteController.class, RestauranteBasicoModel.class);
		this.modelMapper = modelMapper;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@Override
	@NonNull
	public RestauranteBasicoModel toModel(@NonNull Restaurante restaurante) {
		var restauranteModel = createModelWithId(restaurante.getId(), restaurante);
		this.modelMapper.map(restaurante, restauranteModel);
		if (this.algaSecurity.podeConsultarRestaurantes())
			restauranteModel.add(this.algaLinks.linkToRestaurantes("restaurantes"));
		if (this.algaSecurity.podeConsultarCozinhas())
			restauranteModel.getCozinha().add(this.algaLinks.linkToCozinha(restaurante.getCozinha().getId()));
		return restauranteModel;
	}

	@Override
	@NonNull
	public CollectionModel<RestauranteBasicoModel> toCollectionModel(
			@NonNull Iterable<? extends Restaurante> entities) {
		var collectionModel = super.toCollectionModel(entities);
		if (this.algaSecurity.podeConsultarRestaurantes())
			collectionModel.add(this.algaLinks.linkToRestaurantes());
		return collectionModel;
	}
}
