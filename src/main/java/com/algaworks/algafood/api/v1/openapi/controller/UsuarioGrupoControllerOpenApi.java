package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Usuários - Grupos", description = "Gerencia a associação entre usuários e grupos")
public interface UsuarioGrupoControllerOpenApi {

	@Operation(summary = "Lista os grupos de um usuário")
	ResponseEntity<CollectionModel<?>> listar(
			@Parameter(description = "ID de um usuário", example = "1", required = true) Long usuarioId);

	@Operation(summary = "Desassocia um grupo de um usuário")
	ResponseEntity<?> desassociar(
			@Parameter(description = "ID de um usuário", example = "1", required = true) Long usuarioId,
			@Parameter(description = "ID de um grupo", example = "1", required = true) Long grupoId);

	@Operation(summary = "Associa um grupo a um usuário")
	ResponseEntity<?> associar(
			@Parameter(description = "ID de um usuário", example = "1", required = true) Long usuarioId,
			@Parameter(description = "ID de um grupo", example = "1", required = true) Long grupoId);
}
