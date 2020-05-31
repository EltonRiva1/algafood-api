package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.GrupoRepository;

@Service
public class CadastroGrupoService {
	private static final String MSG_GRUPO_EM_USO = "Grupo de código %d não pode ser removido, pois está em uso";
	@Autowired
	private GrupoRepository grupoRepository;
	@Autowired
	private CadastroPermissaoService cadastroPermissaoService;

	@Transactional
	public Grupo salvar(Grupo grupo) {
		// TODO Auto-generated method stub
		return this.grupoRepository.save(grupo);
	}

	@Transactional
	public void excluir(Long grupoId) {
		// TODO Auto-generated method stub
		try {
			this.grupoRepository.deleteById(grupoId);
			this.grupoRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			throw new GrupoNaoEncontradoException(grupoId);
		} catch (DataIntegrityViolationException e) {
			// TODO: handle exception
			throw new EntidadeEmUsoException(String.format(MSG_GRUPO_EM_USO, grupoId));
		}
	}

	@Transactional
	public void desassociarPermissao(Long grupoId, Long permissaoId) {
		Grupo grupo = this.buscarOuFalhar(grupoId);
		Permissao permissao = this.cadastroPermissaoService.buscarOuFalhar(permissaoId);
		grupo.removerPermissao(permissao);
	}

	@Transactional
	public void associarPermissao(Long grupoId, Long permissaoId) {
		Grupo grupo = this.buscarOuFalhar(grupoId);
		Permissao permissao = this.cadastroPermissaoService.buscarOuFalhar(permissaoId);
		grupo.adicionarPermissao(permissao);
	}

	public Grupo buscarOuFalhar(Long grupoId) {
		return this.grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoNaoEncontradoException(grupoId));
	}
}
