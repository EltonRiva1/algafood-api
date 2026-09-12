package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Fluxo de pedidos", description = "Gerencia o fluxo de pedidos")
public interface FluxoPedidoControllerOpenApi {

	@Operation(summary = "Confirma um pedido")
	ResponseEntity<?> confirmar(@Parameter(description = "Código de um pedido", required = true) String codigoPedido);

	@Operation(summary = "Cancela um pedido")
	ResponseEntity<?> cancelar(@Parameter(description = "Código de um pedido", required = true) String codigoPedido);

	@Operation(summary = "Registra a entrega de um pedido")
	ResponseEntity<?> entregar(@Parameter(description = "Código de um pedido", required = true) String codigoPedido);
}
