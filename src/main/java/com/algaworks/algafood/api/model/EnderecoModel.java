package com.algaworks.algafood.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnderecoModel {
	private String cep, logradouro, numero, complemento, bairro;
	private CidadeResumoModel cidade;
}
