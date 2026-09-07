package com.algaworks.algafood.api.v1.assembler;

import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.CidadeController;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Cidade;

@Component
public class CidadeModelAssembler extends RepresentationModelAssemblerSupport<Cidade, CidadeModel> {
	private final ModelMapper mapper;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public CidadeModelAssembler(ModelMapper mapper, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		super(CidadeController.class, CidadeModel.class);
		this.mapper = mapper;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@Override
	@NonNull
	public CidadeModel toModel(@NonNull Cidade cidade) {
		var cidadeModel = createModelWithId(cidade.getId(), cidade);
		this.mapper.map(cidade, cidadeModel);
		if (this.algaSecurity.podeConsultarCidades())
			cidadeModel.add(this.algaLinks.linkToCidades("cidades"));
		if (this.algaSecurity.podeConsultarEstados())
			cidadeModel.getEstado().add(this.algaLinks.linkToEstado(cidadeModel.getEstado().getId()));
		return cidadeModel;
	}

	@Override
	@NonNull
	public CollectionModel<CidadeModel> toCollectionModel(@NonNull Iterable<? extends Cidade> entities) {
		var collectionModel = super.toCollectionModel(entities);
		if (this.algaSecurity.podeConsultarCidades())
			collectionModel.add(this.algaLinks.linkToCidades());
		return collectionModel;
	}
}
