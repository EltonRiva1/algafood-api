package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.input.ProdutoInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Produtos", description = "Gerencia os produtos dos restaurantes")
public interface RestauranteProdutoControllerOpenApi {

	@Operation(summary = "Lista os produtos de um restaurante")
	ResponseEntity<CollectionModel<?>> listar(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId,
			@Parameter(description = "Indica se produtos inativos devem ser incluídos", example = "false") Boolean incluirInativos);

	@Operation(summary = "Busca um produto de um restaurante")
	ResponseEntity<?> buscar(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId,
			@Parameter(description = "ID de um produto", example = "1", required = true) Long produtoId);

	@Operation(summary = "Cadastra um produto em um restaurante")
	ResponseEntity<?> adicionar(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId,
			@RequestBody(description = "Representação de um novo produto", required = true) ProdutoInput produtoInput);

	@Operation(summary = "Atualiza um produto de um restaurante")
	ResponseEntity<?> atualizar(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId,
			@Parameter(description = "ID de um produto", example = "1", required = true) Long produtoId,
			@RequestBody(description = "Representação de um produto com os dados atualizados", required = true) ProdutoInput produtoInput);
}
