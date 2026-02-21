package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AlgaLinks algaLinks;

	public RestauranteModelAssembler() {
		super(RestauranteController.class, RestauranteModel.class);
	}

	@Override
	public RestauranteModel toModel(Restaurante restaurante) {
		var restauranteModel = createModelWithId(restaurante.getId(), restaurante);
		this.modelMapper.map(restaurante, restauranteModel);
		restauranteModel.add(this.algaLinks.linkToRestaurantes("restaurantes"));
		restauranteModel.getCozinha().add(this.algaLinks.linkToCozinha(restaurante.getCozinha().getId()));
		restauranteModel.getEndereco().getCidade()
				.add(this.algaLinks.linkToCidade(restaurante.getEndereco().getCidade().getId()));
		restauranteModel.add(this.algaLinks.linkToRestauranteFormasPagamento(restaurante.getId(), "formas-pagamento"));
		restauranteModel.add(this.algaLinks.linkToRestauranteResponsaveis(restaurante.getId(), "responsaveis"));
		return restauranteModel;
	}

	@Override
	public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
		return super.toCollectionModel(entities).add(this.algaLinks.linkToRestaurantes());
	}
}
