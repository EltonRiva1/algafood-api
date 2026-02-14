package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Pedido;

@Repository
public interface PedidoRepository extends CustomJpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {
	@Query("select distinct p from Pedido p join fetch p.cliente c join fetch p.restaurante r join fetch r.cozinha")
	List<Pedido> findAll();

	@Query("select distinct p from Pedido p join fetch p.cliente c join fetch p.restaurante r join fetch r.cozinha join fetch p.formaPagamento left join fetch p.enderecoEntrega.cidade left join fetch p.itens i left join fetch i.produto where p.codigo = :codigo")
	Optional<Pedido> findByCodigo(String codigo);
}
