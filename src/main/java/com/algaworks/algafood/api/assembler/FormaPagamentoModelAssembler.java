package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoModelAssembler {
	private final ModelMapper mapper;

	public FormaPagamentoModelAssembler(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public FormaPagamentoModel toModel(FormaPagamento formaPagamento) {
		return this.mapper.map(formaPagamento, FormaPagamentoModel.class);
	}

	public List<?> toCollectionModel(Collection<FormaPagamento> formaPagamentos) {
		return formaPagamentos.stream().map(this::toModel).collect(Collectors.toList());
	}
}
