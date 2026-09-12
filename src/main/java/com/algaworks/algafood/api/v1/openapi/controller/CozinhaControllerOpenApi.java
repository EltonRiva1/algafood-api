package com.algaworks.algafood.api.v1.openapi.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.input.CozinhaInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Cozinhas", description = "Gerencia as cozinhas")
public interface CozinhaControllerOpenApi {

	@Operation(summary = "Lista as cozinhas")
	ResponseEntity<PagedModel<?>> listar(@ParameterObject Pageable pageable);

	@Operation(summary = "Busca uma cozinha por ID")
	ResponseEntity<?> buscar(
			@Parameter(description = "ID de uma cozinha", example = "1", required = true) Long cozinhaId);

	@Operation(summary = "Cadastra uma cozinha")
	ResponseEntity<?> adicionar(
			@RequestBody(description = "Representação de uma nova cozinha", required = true) CozinhaInput cozinhaInput);

	@Operation(summary = "Atualiza uma cozinha por ID")
	ResponseEntity<?> atualizar(
			@Parameter(description = "ID de uma cozinha", example = "1", required = true) Long cozinhaId,
			@RequestBody(description = "Representação de uma cozinha com os dados atualizados", required = true) CozinhaInput cozinhaInput);

	@Operation(summary = "Exclui uma cozinha por ID")
	ResponseEntity<?> remover(
			@Parameter(description = "ID de uma cozinha", example = "1", required = true) Long cozinhaId);
}
