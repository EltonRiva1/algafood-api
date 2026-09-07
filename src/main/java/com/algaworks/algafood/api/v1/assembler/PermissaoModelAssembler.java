package com.algaworks.algafood.api.v1.assembler;

import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.model.PermissaoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Permissao;

@Component
public class PermissaoModelAssembler implements RepresentationModelAssembler<Permissao, PermissaoModel> {
	private final ModelMapper mapper;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public PermissaoModelAssembler(ModelMapper mapper, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		this.mapper = mapper;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@Override
	@NonNull
	public PermissaoModel toModel(@NonNull Permissao permissao) {
		return this.mapper.map(permissao, PermissaoModel.class);
	}

	@Override
	@NonNull
	public CollectionModel<PermissaoModel> toCollectionModel(@NonNull Iterable<? extends Permissao> entities) {
		var collectionModel = RepresentationModelAssembler.super.toCollectionModel(entities);
		if (this.algaSecurity.podeConsultarUsuariosGruposPermissoes())
			collectionModel.add(this.algaLinks.linkToPermissoes());
		return collectionModel;
	}
}
