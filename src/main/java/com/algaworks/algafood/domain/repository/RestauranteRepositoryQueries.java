package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;

public interface RestauranteRepositoryQueries {

    List<?> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

    List<?> findComFreteGratis(String nome);
}