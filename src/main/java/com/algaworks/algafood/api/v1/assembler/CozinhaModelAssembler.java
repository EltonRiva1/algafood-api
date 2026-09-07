package com.algaworks.algafood.api.v1.assembler;

import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.CozinhaController;
import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaModelAssembler extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModel> {
	private final ModelMapper mapper;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public CozinhaModelAssembler(ModelMapper mapper, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		super(CozinhaController.class, CozinhaModel.class);
		this.mapper = mapper;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@Override
	@NonNull
	public CozinhaModel toModel(@NonNull Cozinha cozinha) {
		var cozinhaModel = createModelWithId(cozinha.getId(), cozinha);
		this.mapper.map(cozinha, cozinhaModel);
		if (this.algaSecurity.podeConsultarCozinhas())
			cozinhaModel.add(this.algaLinks.linkToCozinhas("cozinhas"));
		return cozinhaModel;
	}
}
