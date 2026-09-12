package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.input.GrupoInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Grupos", description = "Gerencia os grupos de usuários")
public interface GrupoControllerOpenApi {

	@Operation(summary = "Lista os grupos")
	ResponseEntity<CollectionModel<?>> listar();

	@Operation(summary = "Busca um grupo por ID")
	ResponseEntity<?> buscar(@Parameter(description = "ID de um grupo", example = "1", required = true) Long grupoId);

	@Operation(summary = "Cadastra um grupo")
	ResponseEntity<?> adicionar(
			@RequestBody(description = "Representação de um novo grupo", required = true) GrupoInput grupoInput);

	@Operation(summary = "Atualiza um grupo por ID")
	ResponseEntity<?> atualizar(@Parameter(description = "ID de um grupo", example = "1", required = true) Long grupoId,
			@RequestBody(description = "Representação de um grupo com os dados atualizados", required = true) GrupoInput grupoInput);

	@Operation(summary = "Exclui um grupo por ID")
	ResponseEntity<?> remover(@Parameter(description = "ID de um grupo", example = "1", required = true) Long grupoId);
}
