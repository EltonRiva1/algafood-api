package com.algaworks.algafood.api.v1.openapi.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.input.PedidoInput;
import com.algaworks.algafood.domain.filter.PedidoFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Pedidos", description = "Gerencia os pedidos")
public interface PedidoControllerOpenApi {

	@Operation(summary = "Pesquisa os pedidos")
	ResponseEntity<PagedModel<?>> pesquisar(@ParameterObject PedidoFilter pedidoFilter,
			@ParameterObject Pageable pageable);

	@Operation(summary = "Busca um pedido por código")
	ResponseEntity<?> buscar(
			@Parameter(description = "Código de um pedido", example = "f9981ca4-5a5e-4da3-af04-933861df3e55", required = true) String codigoPedido);

	@Operation(summary = "Cadastra um pedido")
	ResponseEntity<?> adicionar(
			@RequestBody(description = "Representação de um novo pedido", required = true) PedidoInput pedidoInput);
}
