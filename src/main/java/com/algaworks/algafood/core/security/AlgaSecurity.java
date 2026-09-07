package com.algaworks.algafood.core.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Component
public class AlgaSecurity {

	private final RestauranteRepository restauranteRepository;
	private final PedidoRepository pedidoRepository;

	public AlgaSecurity(RestauranteRepository restauranteRepository, PedidoRepository pedidoRepository) {
		this.restauranteRepository = restauranteRepository;
		this.pedidoRepository = pedidoRepository;
	}

	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public boolean isAutenticado() {
		var authentication = getAuthentication();
		return authentication != null && authentication.isAuthenticated()
				&& !(authentication instanceof AnonymousAuthenticationToken);
	}

	public Long getUsuarioId() {
		Authentication authentication = getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
			return null;
		}
		Number usuarioId = jwt.getClaim("usuario_id");
		return usuarioId == null ? null : usuarioId.longValue();
	}

	public boolean gerenciaRestaurante(Long restauranteId) {
		var usuarioId = getUsuarioId();
		return usuarioId != null && restauranteRepository.existsResponsavel(restauranteId, usuarioId);
	}

	public boolean gerenciaRestauranteDoPedido(String codigoPedido) {
		Long usuarioId = getUsuarioId();
		return usuarioId != null && pedidoRepository.isPedidoGerenciadoPor(codigoPedido, usuarioId);
	}

	public boolean usuarioAutenticadoIgual(Long usuarioId) {
		Long usuarioAutenticadoId = getUsuarioId();
		return usuarioAutenticadoId != null && usuarioAutenticadoId.equals(usuarioId);
	}

	public boolean hasAuthority(String authorityName) {
		return getAuthentication() != null && getAuthentication().getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(authorityName));
	}

	public boolean temEscopoEscrita() {
		return hasAuthority("SCOPE_WRITE");
	}

	public boolean temEscopoLeitura() {
		return hasAuthority("SCOPE_READ");
	}

	public boolean podeGerenciarPedidos(String codigoPedido) {
		return temEscopoEscrita() && (hasAuthority("GERENCIAR_PEDIDOS") || gerenciaRestauranteDoPedido(codigoPedido));
	}

	public boolean podeConsultarRestaurantes() {
		return temEscopoLeitura() && isAutenticado();
	}

	public boolean podeGerenciarCadastroRestaurantes() {
		return temEscopoEscrita() && hasAuthority("EDITAR_RESTAURANTES");
	}

	public boolean podeGerenciarFuncionamentoRestaurantes(Long restauranteId) {
		return temEscopoEscrita() && (hasAuthority("EDITAR_RESTAURANTES") || gerenciaRestaurante(restauranteId));
	}

	public boolean podeConsultarUsuariosGruposPermissoes() {
		return temEscopoLeitura() && hasAuthority("CONSULTAR_USUARIOS_GRUPOS_PERMISSOES");
	}

	public boolean podeEditarUsuariosGruposPermissoes() {
		return temEscopoEscrita() && hasAuthority("EDITAR_USUARIOS_GRUPOS_PERMISSOES");
	}

	public boolean podePesquisarPedidos(Long clienteId, Long restauranteId) {
		return temEscopoLeitura() && (hasAuthority("CONSULTAR_PEDIDOS") || usuarioAutenticadoIgual(clienteId)
				|| gerenciaRestaurante(restauranteId));
	}

	public boolean podePesquisarPedidos() {
		return isAutenticado() && temEscopoLeitura();
	}

	public boolean podeConsultarFormasPagamento() {
		return temEscopoLeitura() && isAutenticado();
	}

	public boolean podeConsultarCidades() {
		return temEscopoLeitura() && isAutenticado();
	}

	public boolean podeConsultarEstados() {
		return temEscopoLeitura() && isAutenticado();
	}

	public boolean podeConsultarCozinhas() {
		return temEscopoLeitura() && isAutenticado();
	}

	public boolean podeConsultarEstatisticas() {
		return temEscopoLeitura() && hasAuthority("GERAR_RELATORIOS");
	}
}