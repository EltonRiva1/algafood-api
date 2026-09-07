package com.algaworks.algafood.api.v1.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.UsuarioInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.v1.model.input.SenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/v1/usuarios")
public class UsuarioController {
	private final UsuarioRepository usuarioRepository;
	private final CadastroUsuarioService cadastroUsuarioService;
	private final UsuarioModelAssembler usuarioModelAssembler;
	private final UsuarioInputDisassembler usuarioInputDisassembler;

	public UsuarioController(UsuarioRepository usuarioRepository, CadastroUsuarioService cadastroUsuarioService,
			UsuarioModelAssembler usuarioModelAssembler, UsuarioInputDisassembler usuarioInputDisassembler) {
		this.usuarioRepository = usuarioRepository;
		this.cadastroUsuarioService = cadastroUsuarioService;
		this.usuarioModelAssembler = usuarioModelAssembler;
		this.usuarioInputDisassembler = usuarioInputDisassembler;
	}

	@GetMapping
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	public ResponseEntity<CollectionModel<?>> listar() {
		return ResponseEntity.ok(this.usuarioModelAssembler.toCollectionModel(this.usuarioRepository.findAll()));
	}

	@GetMapping("/{usuarioId}")
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	public ResponseEntity<?> buscar(@PathVariable Long usuarioId) {
		return ResponseEntity
				.ok(this.usuarioModelAssembler.toModel(this.cadastroUsuarioService.buscarOuFalhar(usuarioId)));
	}

	@PostMapping
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	public ResponseEntity<?> adicionar(@RequestBody @Valid UsuarioComSenhaInput usuarioComSenhaInput) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.usuarioModelAssembler.toModel(this.cadastroUsuarioService
						.salvar(this.usuarioInputDisassembler.toDomainObject(usuarioComSenhaInput))));
	}

	@PutMapping("/{usuarioId}")
	@CheckSecurity.UsuariosGruposPermissoes.PodeAlterarUsuario
	public ResponseEntity<?> atualizar(@PathVariable Long usuarioId, @RequestBody @Valid UsuarioInput usuarioInput) {
		var usuarioAtual = this.cadastroUsuarioService.buscarOuFalhar(usuarioId);
		this.usuarioInputDisassembler.copyToDomainObject(usuarioInput, usuarioAtual);
		return ResponseEntity.ok(this.usuarioModelAssembler.toModel(this.cadastroUsuarioService.salvar(usuarioAtual)));
	}

	@PutMapping("/{usuarioId}/senha")
	@CheckSecurity.UsuariosGruposPermissoes.PodeAlterarPropriaSenha
	public ResponseEntity<?> alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInput senhaInput) {
		this.cadastroUsuarioService.alterarSenha(usuarioId, senhaInput.getSenhaAtual(), senhaInput.getNovaSenha());
		return ResponseEntity.noContent().build();
	}
}
