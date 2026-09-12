package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import com.algaworks.algafood.api.v1.model.input.FormaPagamentoInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Formas de pagamento", description = "Gerencia as formas de pagamento")
public interface FormaPagamentoControllerOpenApi {

	@Operation(summary = "Lista as formas de pagamento")
	ResponseEntity<CollectionModel<?>> listar(@Parameter(hidden = true) ServletWebRequest request);

	@Operation(summary = "Busca uma forma de pagamento por ID")
	ResponseEntity<?> buscar(
			@Parameter(description = "ID de uma forma de pagamento", example = "1", required = true) Long formaPagamentoId,
			@Parameter(hidden = true) ServletWebRequest request);

	@Operation(summary = "Cadastra uma forma de pagamento")
	ResponseEntity<?> adicionar(
			@RequestBody(description = "Representação de uma nova forma de pagamento", required = true) FormaPagamentoInput formaPagamentoInput);

	@Operation(summary = "Atualiza uma forma de pagamento por ID")
	ResponseEntity<?> atualizar(
			@Parameter(description = "ID de uma forma de pagamento", example = "1", required = true) Long formaPagamentoId,
			@RequestBody(description = "Representação de uma forma de pagamento com os dados atualizados", required = true) FormaPagamentoInput formaPagamentoInput);

	@Operation(summary = "Exclui uma forma de pagamento por ID")
	ResponseEntity<?> remover(
			@Parameter(description = "ID de uma forma de pagamento", example = "1", required = true) Long formaPagamentoId);
}
