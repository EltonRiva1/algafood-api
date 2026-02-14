package com.algaworks.algafood.domain.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService {
	private final UsuarioRepository usuarioRepository;
	private final CadastroGrupoService cadastroGrupoService;

	public CadastroUsuarioService(UsuarioRepository usuarioRepository, CadastroGrupoService cadastroGrupoService) {
		this.usuarioRepository = usuarioRepository;
		this.cadastroGrupoService = cadastroGrupoService;
	}

	@Transactional
	public Usuario salvar(Usuario usuario) {
		Optional<?> usuarioExistente = this.usuarioRepository.findByEmail(usuario.getEmail());
		if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario))
			throw new NegocioException(
					String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail()));
		return this.usuarioRepository.save(usuario);
	}

	@Transactional
	public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
		var usuario = this.buscarOuFalhar(usuarioId);
		if (usuario.senhaNaoCoincideCom(senhaAtual))
			throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
		usuario.setSenha(novaSenha);
	}

	public Usuario buscarOuFalhar(Long usuarioId) {
		return this.usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
	}

	@Transactional
	public void desassociarGrupo(Long usuarioId, Long grupoId) {
		var usuario = this.buscarOuFalhar(usuarioId);
		var grupo = this.cadastroGrupoService.buscarOuFalhar(grupoId);
		usuario.removerGrupo(grupo);
	}

	@Transactional
	public void associarGrupo(Long usuarioId, Long grupoId) {
		var usuario = this.buscarOuFalhar(usuarioId);
		var grupo = this.cadastroGrupoService.buscarOuFalhar(grupoId);
		usuario.adicionarGrupo(grupo);
	}

	public Usuario buscarComGruposOuFalhar(Long usuarioId) {
		return this.usuarioRepository.findByIdWithGrupos(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
	}
}
