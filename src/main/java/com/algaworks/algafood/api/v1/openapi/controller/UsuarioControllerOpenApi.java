package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.input.SenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Usuários", description = "Gerencia os usuários")
public interface UsuarioControllerOpenApi {

	@Operation(summary = "Lista os usuários")
	ResponseEntity<CollectionModel<?>> listar();

	@Operation(summary = "Busca um usuário por ID")
	ResponseEntity<?> buscar(
			@Parameter(description = "ID de um usuário", example = "1", required = true) Long usuarioId);

	@Operation(summary = "Cadastra um usuário")
	ResponseEntity<?> adicionar(
			@RequestBody(description = "Representação de um novo usuário", required = true) UsuarioComSenhaInput usuarioComSenhaInput);

	@Operation(summary = "Atualiza um usuário")
	ResponseEntity<?> atualizar(
			@Parameter(description = "ID de um usuário", example = "1", required = true) Long usuarioId,
			@RequestBody(description = "Representação de um usuário com os dados atualizados", required = true) UsuarioInput usuarioInput);

	@Operation(summary = "Altera a senha de um usuário")
	ResponseEntity<?> alterarSenha(
			@Parameter(description = "ID de um usuário", example = "1", required = true) Long usuarioId,
			@RequestBody(description = "Senhas atual e nova", required = true) SenhaInput senhaInput);
}
