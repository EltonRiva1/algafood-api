package com.algaworks.algafood.core.security.authorizationserver;

import com.algaworks.algafood.domain.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JpaUserDetailsService implements UserDetailsService {
	private final UsuarioRepository usuarioRepository;

	public JpaUserDetailsService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.usuarioRepository.buscarPorEmailComGruposPermissoes(username).map(AuthUser::new)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
	}
}
