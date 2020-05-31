package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProdutoModel {
	private Long id;
	private String nome, descricao;
	private BigDecimal preco;
	private Boolean ativo;
}
