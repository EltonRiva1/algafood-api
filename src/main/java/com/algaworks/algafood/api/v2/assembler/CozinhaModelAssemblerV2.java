package com.algaworks.algafood.api.v2.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v2.AlgaLinksV2;
import com.algaworks.algafood.api.v2.controller.CozinhaControllerV2;
import com.algaworks.algafood.api.v2.model.CozinhaModelV2;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaModelAssemblerV2 extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModelV2> {
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private AlgaLinksV2 algaLinksV2;

	public CozinhaModelAssemblerV2() {
		super(CozinhaControllerV2.class, CozinhaModelV2.class);
	}

	@Override
	public CozinhaModelV2 toModel(Cozinha cozinha) {
		var cozinhaModel = this.createModelWithId(cozinha.getId(), cozinha);
		this.mapper.map(cozinha, cozinhaModel);
		cozinhaModel.add(this.algaLinksV2.linkToCozinhas("cozinhas"));
		return cozinhaModel;
	}
}
