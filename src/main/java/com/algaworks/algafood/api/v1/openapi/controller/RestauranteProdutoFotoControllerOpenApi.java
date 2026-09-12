package com.algaworks.algafood.api.v1.openapi.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import com.algaworks.algafood.api.v1.model.input.FotoProdutoInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Fotos de produtos", description = "Gerencia as fotos dos produtos")
public interface RestauranteProdutoFotoControllerOpenApi {

	@Operation(summary = "Atualiza a foto de um produto")
	ResponseEntity<?> atualizarFoto(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId,
			@Parameter(description = "ID de um produto", example = "1", required = true) Long produtoId,
			@RequestBody(required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = FotoProdutoInput.class))) FotoProdutoInput fotoProdutoInput)
			throws IOException;

	@Operation(summary = "Busca os metadados da foto de um produto")
	ResponseEntity<?> buscar(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId,
			@Parameter(description = "ID de um produto", example = "1", required = true) Long produtoId);

	@Operation(summary = "Serve o conteúdo binário da foto de um produto", responses = @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)))
	ResponseEntity<?> servirFoto(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId,
			@Parameter(description = "ID de um produto", example = "1", required = true) Long produtoId,
			@Parameter(description = "Media types aceitos pelo consumidor", example = "image/jpeg") String acceptHeader)
			throws HttpMediaTypeNotAcceptableException;

	@Operation(summary = "Exclui a foto de um produto")
	ResponseEntity<?> excluir(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId,
			@Parameter(description = "ID de um produto", example = "1", required = true) Long produtoId);
}
