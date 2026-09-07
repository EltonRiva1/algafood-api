package com.algaworks.algafood.api.v1.assembler;

import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.EstadoController;
import com.algaworks.algafood.api.v1.model.EstadoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class EstadoModelAssembler extends RepresentationModelAssemblerSupport<Estado, EstadoModel> {
	private final ModelMapper mapper;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public EstadoModelAssembler(ModelMapper mapper, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		super(EstadoController.class, EstadoModel.class);
		this.mapper = mapper;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@Override
	@NonNull
	public EstadoModel toModel(@NonNull Estado estado) {
		var estadoModel = createModelWithId(estado.getId(), estado);
		this.mapper.map(estado, estadoModel);
		if (this.algaSecurity.podeConsultarEstados())
			estadoModel.add(this.algaLinks.linkToEstados("estados"));
		return estadoModel;
	}

	@Override
	@NonNull
	public CollectionModel<EstadoModel> toCollectionModel(@NonNull Iterable<? extends Estado> entities) {
		var collectionModel = super.toCollectionModel(entities);
		if (algaSecurity.podeConsultarEstados())
			collectionModel.add(algaLinks.linkToEstados());
		return collectionModel;
	}
}
