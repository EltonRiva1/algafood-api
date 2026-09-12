package com.algaworks.algafood.api.v1.openapi.controller;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.input.RestauranteInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Restaurantes", description = "Gerencia os restaurantes")
public interface RestauranteControllerOpenApi {

	@Operation(summary = "Lista os restaurantes")
	ResponseEntity<CollectionModel<?>> listar(
			@Parameter(in = ParameterIn.QUERY, name = "projecao", description = "Nome da projeção de restaurantes", schema = @Schema(allowableValues = {
					"apenas-nome" })) String projecao);

	@Operation(summary = "Busca um restaurante por ID")
	ResponseEntity<?> buscar(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

	@Operation(summary = "Cadastra um restaurante")
	ResponseEntity<?> adicionar(
			@RequestBody(description = "Representação de um novo restaurante", required = true) RestauranteInput restauranteInput);

	@Operation(summary = "Atualiza um restaurante por ID")
	ResponseEntity<?> atualizar(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId,
			@RequestBody(description = "Representação de um restaurante com os dados atualizados", required = true) RestauranteInput restauranteInput);

	@Operation(summary = "Ativa um restaurante")
	ResponseEntity<?> ativar(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

	@Operation(summary = "Inativa um restaurante")
	ResponseEntity<?> inativar(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

	@Operation(summary = "Abre um restaurante")
	ResponseEntity<?> abrir(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

	@Operation(summary = "Fecha um restaurante")
	ResponseEntity<?> fechar(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

	@Operation(summary = "Ativa vários restaurantes")
	ResponseEntity<?> ativarMultiplos(
			@RequestBody(description = "IDs dos restaurantes", required = true) List<Long> restauranteIds);

	@Operation(summary = "Inativa vários restaurantes")
	ResponseEntity<?> inativarMultiplos(
			@RequestBody(description = "IDs dos restaurantes", required = true) List<Long> restauranteIds);

	@Operation(summary = "Lista os restaurantes usando a projeção apenas-nome")
	ResponseEntity<CollectionModel<?>> listarApenasNome();
}
