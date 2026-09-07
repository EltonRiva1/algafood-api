package com.algaworks.algafood.core.security.authorizationserver;

import com.algaworks.algafood.domain.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

public class AuthUser implements UserDetails {
	@Serial
	private static final long serialVersionUID = 1L;

	private final Usuario usuario;
	private final List<GrantedAuthority> authorities;

	public AuthUser(Usuario usuario) {
		this.usuario = usuario;
		this.authorities = usuario.getGrupos().stream().flatMap(grupo -> grupo.getPermissoes().stream())
				.map(permissao -> new SimpleGrantedAuthority(permissao.getNome())).distinct()
				.map(authority -> (GrantedAuthority) authority).toList();
	}

	public Long getUsuarioId() {
		return usuario.getId();
	}

	public String getNomeCompleto() {
		return usuario.getNome();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return usuario.getSenha();
	}

	@Override
	public String getUsername() {
		return usuario.getEmail();
	}
}
