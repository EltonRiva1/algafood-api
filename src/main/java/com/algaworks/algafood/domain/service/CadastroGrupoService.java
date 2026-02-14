package com.algaworks.algafood.domain.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;

@Service
public class CadastroGrupoService {
	private static final String MSG_GRUPO_EM_USO = "Grupo de código %d não pode ser removido, pois está em uso";
	private final GrupoRepository grupoRepository;
	private final CadastroPermissaoService cadastroPermissaoService;

	public CadastroGrupoService(GrupoRepository grupoRepository, CadastroPermissaoService cadastroPermissaoService) {
		this.grupoRepository = grupoRepository;
		this.cadastroPermissaoService = cadastroPermissaoService;
	}

	@Transactional
	public Grupo salvar(Grupo grupo) {
		return this.grupoRepository.save(grupo);
	}

	@Transactional
	public void excluir(Long grupoId) {
		if (!this.grupoRepository.existsById(grupoId)) {
			throw new GrupoNaoEncontradoException(grupoId);
		}
		try {
			this.grupoRepository.deleteById(grupoId);
			this.grupoRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_GRUPO_EM_USO, grupoId));
		}
	}

	public Grupo buscarOuFalhar(Long grupoId) {
		return this.grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoNaoEncontradoException(grupoId));
	}

	@Transactional
	public void desassociarPermissao(Long grupoId, Long permissaoId) {
		var grupo = this.buscarOuFalhar(grupoId);
		var permissao = this.cadastroPermissaoService.buscarOuFalhar(permissaoId);
		grupo.removerPermissao(permissao);
	}

	@Transactional
	public void associarPermissao(Long grupoId, Long permissaoId) {
		var grupo = this.buscarOuFalhar(grupoId);
		var permissao = this.cadastroPermissaoService.buscarOuFalhar(permissaoId);
		grupo.adicionarPermissao(permissao);
	}

	public Grupo buscarComPermissoesOuFalhar(Long grupoId) {
		return this.grupoRepository.findByIdWithPermissoes(grupoId)
				.orElseThrow(() -> new GrupoNaoEncontradoException(grupoId));
	}
}
