package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Restaurantes - Responsáveis", description = "Gerencia os usuários responsáveis por restaurantes")
public interface RestauranteUsuarioResponsavelControllerOpenApi {

	@Operation(summary = "Lista os responsáveis por um restaurante")
	ResponseEntity<CollectionModel<?>> listar(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

	@Operation(summary = "Desassocia um responsável de um restaurante")
	ResponseEntity<?> desassociar(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId,
			@Parameter(description = "ID de um usuário", example = "1", required = true) Long usuarioId);

	@Operation(summary = "Associa um responsável a um restaurante")
	ResponseEntity<?> associar(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId,
			@Parameter(description = "ID de um usuário", example = "1", required = true) Long usuarioId);
}
