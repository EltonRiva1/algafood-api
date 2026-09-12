package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.input.CidadeInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Cidades", description = "Gerencia as cidades")
public interface CidadeControllerOpenApi {

	@Operation(summary = "Lista as cidades")
	ResponseEntity<CollectionModel<?>> listar();

	@Operation(summary = "Busca uma cidade por ID", responses = @ApiResponse(responseCode = "200"))
	ResponseEntity<?> buscar(
			@Parameter(description = "ID de uma cidade", example = "1", required = true) Long cidadeId);

	@Operation(summary = "Cadastra uma cidade")
	ResponseEntity<?> adicionar(
			@RequestBody(description = "Representação de uma nova cidade", required = true) CidadeInput cidadeInput);

	@Operation(summary = "Atualiza uma cidade por ID")
	ResponseEntity<?> atualizar(
			@Parameter(description = "ID de uma cidade", example = "1", required = true) Long cidadeId,
			@RequestBody(description = "Representação de uma cidade com os dados atualizados", required = true) CidadeInput cidadeInput);

	@Operation(summary = "Exclui uma cidade por ID")
	ResponseEntity<?> remover(
			@Parameter(description = "ID de uma cidade", example = "1", required = true) Long cidadeId);
}
