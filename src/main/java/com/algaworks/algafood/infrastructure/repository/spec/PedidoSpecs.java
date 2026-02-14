package com.algaworks.algafood.infrastructure.repository.spec;

import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class PedidoSpecs {
	public static Specification<Pedido> usandoFiltro(PedidoFilter pedidoFilter) {
		return (root, query, builder) -> {
			if (Pedido.class.equals(query.getResultType())) {
				root.fetch("restaurante").fetch("cozinha");
				root.fetch("cliente");
			}
			var predicates = new ArrayList<Predicate>();
			if (pedidoFilter.getClienteId() != null)
				predicates.add(builder.equal(root.get("cliente").get("id"), pedidoFilter.getClienteId()));
			if (pedidoFilter.getRestauranteId() != null)
				predicates.add(builder.equal(root.get("restaurante").get("id"), pedidoFilter.getRestauranteId()));
			if (pedidoFilter.getDataCriacaoInicio() != null)
				predicates.add(
						builder.greaterThanOrEqualTo(root.get("dataCriacao"), pedidoFilter.getDataCriacaoInicio()));
			if (pedidoFilter.getDataCriacaoFim() != null)
				predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), pedidoFilter.getDataCriacaoFim()));
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
