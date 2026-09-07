package com.algaworks.algafood.api.v1.assembler;

import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.GrupoController;
import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Grupo;

@Component
public class GrupoModelAssembler extends RepresentationModelAssemblerSupport<Grupo, GrupoModel> {
	private final ModelMapper mapper;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public GrupoModelAssembler(ModelMapper mapper, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		super(GrupoController.class, GrupoModel.class);
		this.mapper = mapper;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@Override
	@NonNull
	public GrupoModel toModel(@NonNull Grupo grupo) {
		var grupoModel = createModelWithId(grupo.getId(), grupo);
		this.mapper.map(grupo, grupoModel);
		if (this.algaSecurity.podeConsultarUsuariosGruposPermissoes())
			grupoModel.add(this.algaLinks.linkToGrupos("grupos"))
					.add(this.algaLinks.linkToGrupoPermissoes(grupo.getId(), "permissoes"));
		return grupoModel;
	}

	@Override
	@NonNull
	public CollectionModel<GrupoModel> toCollectionModel(@NonNull Iterable<? extends Grupo> entities) {
		var collectionModel = super.toCollectionModel(entities);
		if (this.algaSecurity.podeConsultarUsuariosGruposPermissoes())
			collectionModel.add(this.algaLinks.linkToGrupos());
		return collectionModel;
	}
}
