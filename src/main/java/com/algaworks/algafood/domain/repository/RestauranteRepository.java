package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries,
		JpaSpecificationExecutor<Restaurante> {
	List<?> queryByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

	List<?> consultarPorNome(String nome, @Param("id") Long cozinha);

	Optional<?> findFirstRestauranteByNomeContaining(String nome);

	List<?> findTop2ByNomeContaining(String nome);

	int countByCozinhaId(Long cozinha);

	@Query("from Restaurante r join fetch r.cozinha")
	List<Restaurante> findAll();

	@Query("from Restaurante r join fetch r.cozinha left join fetch r.endereco.cidade where r.id = :id")
	Optional<Restaurante> findByIdFetchingEnderecoCidade(@Param("id") Long id);

	@Query("from Restaurante r join fetch r.cozinha left join fetch r.endereco.cidade")
	List<Restaurante> findAllFetchingEnderecoCidade();

	@Query("select distinct r from Restaurante r join fetch r.cozinha left join fetch r.endereco.cidade left join fetch r.formasPagamento where r.id = :id")
	Optional<Restaurante> findByIdFetchingFormasPagamento(@Param("id") Long id);

	@Query("select distinct r from Restaurante r join fetch r.cozinha left join fetch r.endereco.cidade left join fetch r.responsaveis where r.id = :id")
	Optional<Restaurante> findByIdFetchingResponsaveis(@Param("id") Long id);

}
