package com.algaworks.algafood.infrastructure.repository;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.algaworks.algafood.domain.repository.CustomJpaRepository;

import lombok.var;

public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements CustomJpaRepository<T, ID> {
	private EntityManager entityManager;

	public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		// TODO Auto-generated constructor stub
		this.entityManager = entityManager;
	}

	@Override
	public Optional<T> buscarPrimeiro() {
		// TODO Auto-generated method stub
		var jpql = "from " + this.getDomainClass().getName();
		T entity = this.entityManager.createQuery(jpql, this.getDomainClass()).setMaxResults(1).getSingleResult();
		return Optional.ofNullable(entity);
	}

	@Override
	public void detach(T entity) {
		// TODO Auto-generated method stub
		this.entityManager.detach(entity);
	}

}
