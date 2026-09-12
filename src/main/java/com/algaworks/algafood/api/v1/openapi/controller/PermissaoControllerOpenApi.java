package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Permissões", description = "Consulta as permissões")
public interface PermissaoControllerOpenApi {

	@Operation(summary = "Lista as permissões")
	ResponseEntity<CollectionModel<?>> listar();
}
