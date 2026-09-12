package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Grupos - Permissões", description = "Gerencia a associação entre grupos e permissões")
public interface GrupoPermissaoControllerOpenApi {

	@Operation(summary = "Lista as permissões de um grupo")
	ResponseEntity<CollectionModel<?>> listar(
			@Parameter(description = "ID de um grupo", example = "1", required = true) Long grupoId);

	@Operation(summary = "Desassocia uma permissão de um grupo")
	ResponseEntity<?> desassociar(
			@Parameter(description = "ID de um grupo", example = "1", required = true) Long grupoId,
			@Parameter(description = "ID de uma permissão", example = "1", required = true) Long permissaoId);

	@Operation(summary = "Associa uma permissão a um grupo")
	ResponseEntity<?> associar(@Parameter(description = "ID de um grupo", example = "1", required = true) Long grupoId,
			@Parameter(description = "ID de uma permissão", example = "1", required = true) Long permissaoId);
}
