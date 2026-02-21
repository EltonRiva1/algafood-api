package com.algaworks.algafood.api;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.TemplateVariable.VariableType;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.CidadeController;
import com.algaworks.algafood.api.controller.CozinhaController;
import com.algaworks.algafood.api.controller.EstadoController;
import com.algaworks.algafood.api.controller.FluxoPedidoController;
import com.algaworks.algafood.api.controller.FormaPagamentoController;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.controller.RestauranteFormaPagamentoController;
import com.algaworks.algafood.api.controller.RestauranteProdutoController;
import com.algaworks.algafood.api.controller.RestauranteUsuarioResponsavelController;
import com.algaworks.algafood.api.controller.UsuarioController;
import com.algaworks.algafood.api.controller.UsuarioGrupoController;

@Component
public class AlgaLinks {
	private static final TemplateVariables PAGINACAO_VARIABLES = new TemplateVariables(
			new TemplateVariable("page", VariableType.REQUEST_PARAM),
			new TemplateVariable("size", VariableType.REQUEST_PARAM),
			new TemplateVariable("sort", VariableType.REQUEST_PARAM));

	public Link linkToPedidos() {
		var filtroVariables = new TemplateVariables(new TemplateVariable("clienteId", VariableType.REQUEST_PARAM),
				new TemplateVariable("restauranteId", VariableType.REQUEST_PARAM),
				new TemplateVariable("dataCriacaoInicio", VariableType.REQUEST_PARAM),
				new TemplateVariable("dataCriacaoFim", VariableType.REQUEST_PARAM));
		return Link.of(UriTemplate.of(WebMvcLinkBuilder.linkTo(PedidoController.class).toUri().toString(),
				PAGINACAO_VARIABLES.concat(filtroVariables)), "pedidos");
	}

	public Link linkToConfirmacaoPedido(String codigoPedido, String rel) {
		return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FluxoPedidoController.class).confirmar(codigoPedido))
				.withRel(rel);
	}

	public Link linkToEntregaPedido(String codigoPedido, String rel) {
		return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FluxoPedidoController.class).entregar(codigoPedido))
				.withRel(rel);
	}

	public Link linkToCancelamentoPedido(String codigoPedido, String rel) {
		return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FluxoPedidoController.class).cancelar(codigoPedido))
				.withRel(rel);
	}

	public Link linkToRestaurante(Long restauranteId, String rel) {
		return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RestauranteController.class).buscar(restauranteId))
				.withRel(rel);
	}

	public Link linkToRestaurante(Long restauranteId) {
		return this.linkToRestaurante(restauranteId, IanaLinkRelations.SELF_VALUE);
	}

	public Link linkToRestaurantes(String rel) {
		return WebMvcLinkBuilder.linkTo(RestauranteController.class).withRel(rel);
	}

	public Link linkToRestaurantes() {
		return this.linkToRestaurantes(IanaLinkRelations.SELF.value());
	}

	public Link linkToRestauranteFormasPagamento(Long restauranteId, String rel) {
		return WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(RestauranteFormaPagamentoController.class).listar(restauranteId))
				.withRel(rel);
	}

	public Link linkToUsuario(Long usuarioId, String rel) {
		return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class).buscar(usuarioId))
				.withRel(rel);
	}

	public Link linkToUsuario(Long usuarioId) {
		return this.linkToUsuario(usuarioId, IanaLinkRelations.SELF_VALUE);
	}

	public Link linkToUsuarios(String rel) {
		return WebMvcLinkBuilder.linkTo(UsuarioController.class).withRel(rel);
	}

	public Link linkToUsuarios() {
		return this.linkToUsuarios(IanaLinkRelations.SELF_VALUE);
	}

	public Link linkToGruposUsuario(Long usuarioId, String rel) {
		return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioGrupoController.class).listar(usuarioId))
				.withRel(rel);
	}

	public Link linkToGruposUsuario(Long usuarioId) {
		return this.linkToGruposUsuario(usuarioId, IanaLinkRelations.SELF_VALUE);
	}

	public Link linkToRestauranteResponsaveis(Long restauranteId, String rel) {
		return WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(RestauranteUsuarioResponsavelController.class).listar(restauranteId))
				.withRel(rel);
	}

	public Link linkToResponsaveisRestaurante(Long restauranteId, String rel) {
		return WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(RestauranteUsuarioResponsavelController.class).listar(restauranteId))
				.withRel(rel);
	}

	public Link linkToResponsaveisRestaurante(Long restauranteId) {
		return this.linkToResponsaveisRestaurante(restauranteId, IanaLinkRelations.SELF.value());
	}

	public Link linkToFormaPagamento(Long formaPagamentoId, String rel) {
		return WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(FormaPagamentoController.class).buscar(formaPagamentoId, null))
				.withRel(rel);
	}

	public Link linkToFormaPagamento(Long formaPagamentoId) {
		return this.linkToFormaPagamento(formaPagamentoId, IanaLinkRelations.SELF.value());
	}

	public Link linkToCidade(Long cidadeId, String rel) {
		return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CidadeController.class).buscar(cidadeId))
				.withRel(rel);
	}

	public Link linkToCidade(Long cidadeId) {
		return this.linkToCidade(cidadeId, IanaLinkRelations.SELF.value());
	}

	public Link linkToCidades(String rel) {
		return WebMvcLinkBuilder.linkTo(CidadeController.class).withRel(rel);
	}

	public Link linkToCidades() {
		return this.linkToCidades(IanaLinkRelations.SELF.value());
	}

	public Link linkToEstado(Long estadoId, String rel) {
		return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EstadoController.class).buscar(estadoId))
				.withRel(rel);
	}

	public Link linkToEstado(Long estadoId) {
		return this.linkToEstado(estadoId, IanaLinkRelations.SELF.value());
	}

	public Link linkToEstados(String rel) {
		return WebMvcLinkBuilder.linkTo(EstadoController.class).withRel(rel);
	}

	public Link linkToEstados() {
		return this.linkToEstados(IanaLinkRelations.SELF.value());
	}

	public Link linkToProduto(Long restauranteId, Long produtoId, String rel) {
		return WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(RestauranteProdutoController.class).buscar(restauranteId, produtoId))
				.withRel(rel);
	}

	public Link linkToProduto(Long restauranteId, Long produtoId) {
		return this.linkToProduto(restauranteId, produtoId, IanaLinkRelations.SELF.value());
	}

	public Link linkToCozinhas(String rel) {
		return WebMvcLinkBuilder.linkTo(CozinhaController.class).withRel(rel);
	}

	public Link linkToCozinhas() {
		return this.linkToCozinhas(IanaLinkRelations.SELF.value());
	}

	public Link linkToCozinha(Long cozinhaId, String rel) {
		return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CozinhaController.class).buscar(cozinhaId))
				.withRel(rel);
	}

	public Link linkToCozinha(Long cozinhaId) {
		return this.linkToCozinha(cozinhaId, IanaLinkRelations.SELF.value());
	}
}
