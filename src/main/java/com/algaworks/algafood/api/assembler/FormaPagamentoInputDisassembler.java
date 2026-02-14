package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoInputDisassembler {
	private final ModelMapper mapper;

	public FormaPagamentoInputDisassembler(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public FormaPagamento toDomainObject(FormaPagamentoInput formaPagamentoInput) {
		return this.mapper.map(formaPagamentoInput, FormaPagamento.class);
	}

	public void copyToDomainObject(FormaPagamentoInput formaPagamentoInput, FormaPagamento formaPagamento) {
		this.mapper.map(formaPagamentoInput, formaPagamento);
	}
}
