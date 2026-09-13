package com.algaworks.algafood.core.security.authorizationserver;

import com.algaworks.algafood.domain.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AuthUser implements UserDetails {
	@Serial
	private static final long serialVersionUID = 1L;
	private final long usuarioId;
	private final String nomeCompleto;
	private final String username;
	private final String password;
	private final List<GrantedAuthority> authorities;

	public AuthUser(Usuario usuario) {
		this.usuarioId = usuario.getId();
		this.nomeCompleto = usuario.getNome();
		this.username = usuario.getEmail();
		this.password = usuario.getSenha();
		this.authorities = usuario.getGrupos().stream().flatMap(grupo -> grupo.getPermissoes().stream())
				.map(permissao -> new SimpleGrantedAuthority(permissao.getNome())).distinct()
				.map(authority -> (GrantedAuthority) authority).collect(Collectors.toCollection(ArrayList::new));
	}

	public AuthUser(long usuarioId, String nomeCompleto, String username, String password,
			List<GrantedAuthority> authorities) {
		this.usuarioId = usuarioId;
		this.nomeCompleto = nomeCompleto;
		this.username = username;
		this.password = password;
		this.authorities = new ArrayList<>(authorities);
	}

	public long getUsuarioId() {
		return usuarioId;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
}
