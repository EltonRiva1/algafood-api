package com.algaworks.algafood.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Usuario;

@Repository
public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long> {
	Optional<Usuario> findByEmail(String email);

	@Query("select distinct u from Usuario u left join fetch u.grupos where u.id = :id")
	Optional<Usuario> findByIdWithGrupos(@Param("id") Long id);

	@Query("""
			select distinct u from Usuario u
			left join fetch u.grupos g
			left join fetch g.permissoes
			where u.email = :email
			""")
	Optional<Usuario> buscarPorEmailComGruposPermissoes(@Param("email") String email);
}
