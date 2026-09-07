package com.algaworks.algafood.api.v1.assembler;

import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.UsuarioController;
import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Usuario;

@Component
public class UsuarioModelAssembler extends RepresentationModelAssemblerSupport<Usuario, UsuarioModel> {
	private final ModelMapper mapper;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public UsuarioModelAssembler(ModelMapper mapper, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		super(UsuarioController.class, UsuarioModel.class);
		this.mapper = mapper;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@Override
	@NonNull
	public UsuarioModel toModel(@NonNull Usuario usuario) {
		var usuarioModel = createModelWithId(usuario.getId(), usuario);
		this.mapper.map(usuario, usuarioModel);
		if (this.algaSecurity.podeConsultarUsuariosGruposPermissoes())
			usuarioModel.add(this.algaLinks.linkToUsuarios("usuarios"))
					.add(this.algaLinks.linkToGruposUsuario(usuario.getId(), "grupos-usuario"));
		return usuarioModel;
	}

	@Override
	@NonNull
	public CollectionModel<UsuarioModel> toCollectionModel(@NonNull Iterable<? extends Usuario> entities) {
		var collectionModel = super.toCollectionModel(entities);
		if (this.algaSecurity.podeConsultarUsuariosGruposPermissoes())
			collectionModel.add(this.algaLinks.linkToUsuarios());
		return collectionModel;
	}
}
