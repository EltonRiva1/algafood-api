package com.algaworks.algafood.infrastructure.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepositoryQueries;

import jakarta.persistence.EntityManager;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryQueries {
	private final EntityManager entityManager;

	public ProdutoRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional
	public FotoProduto save(FotoProduto fotoProduto) {
		return this.entityManager.merge(fotoProduto);
	}

	@Override
	@Transactional
	public void delete(FotoProduto fotoProduto) {
		this.entityManager.remove(fotoProduto);
	}
}
