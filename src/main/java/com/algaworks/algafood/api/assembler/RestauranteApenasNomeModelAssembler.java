package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.model.RestauranteApenasNomeModel;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteApenasNomeModelAssembler
		extends RepresentationModelAssemblerSupport<Restaurante, RestauranteApenasNomeModel> {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private AlgaLinks algaLinks;

	public RestauranteApenasNomeModelAssembler() {
		super(RestauranteController.class, RestauranteApenasNomeModel.class);
	}

	@Override
	public RestauranteApenasNomeModel toModel(Restaurante restaurante) {
		var restauranteModel = this.createModelWithId(restaurante.getId(), restaurante);
		this.modelMapper.map(restaurante, restauranteModel);
		restauranteModel.add(this.algaLinks.linkToRestaurantes("restaurantes"));
		return restauranteModel;
	}

	@Override
	public CollectionModel<RestauranteApenasNomeModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
		return super.toCollectionModel(entities).add(this.algaLinks.linkToRestaurantes());
	}
}
