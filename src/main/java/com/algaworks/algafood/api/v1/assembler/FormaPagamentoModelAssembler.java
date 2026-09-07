package com.algaworks.algafood.api.v1.assembler;

import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.FormaPagamentoController;
import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoModelAssembler
		extends RepresentationModelAssemblerSupport<FormaPagamento, FormaPagamentoModel> {
	private final ModelMapper mapper;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public FormaPagamentoModelAssembler(ModelMapper mapper, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		super(FormaPagamentoController.class, FormaPagamentoModel.class);
		this.mapper = mapper;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@Override
	@NonNull
	public FormaPagamentoModel toModel(@NonNull FormaPagamento formaPagamento) {
		var formaPagamentoModel = createModelWithId(formaPagamento.getId(), formaPagamento);
		this.mapper.map(formaPagamento, formaPagamentoModel);
		if (this.algaSecurity.podeConsultarFormasPagamento())
			formaPagamentoModel.add(this.algaLinks.linkToFormasPagamento("formasPagamento"));
		return formaPagamentoModel;
	}

	@Override
	@NonNull
	public CollectionModel<FormaPagamentoModel> toCollectionModel(
			@NonNull Iterable<? extends FormaPagamento> entities) {
		var collectionModel = super.toCollectionModel(entities);
		if (this.algaSecurity.podeConsultarFormasPagamento())
			collectionModel.add(this.algaLinks.linkToFormasPagamento());
		return collectionModel;
	}
}
