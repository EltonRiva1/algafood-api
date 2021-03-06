package com.algaworks.algafood.api.model.input;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnderecoInput {
	@NotBlank
	private String cep, logradouro, numero, bairro;
	private String complemento;
	@Valid
	@NotNull
	private CidadeIdInput cidade;
}
