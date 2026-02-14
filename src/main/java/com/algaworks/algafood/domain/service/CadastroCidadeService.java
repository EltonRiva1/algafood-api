package com.algaworks.algafood.domain.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;

@Service
public class CadastroCidadeService {
	private static final String MSG_CIDADE_EM_USO = "Cidade de código %d não pode ser removida, pois está em uso";
	private final CidadeRepository cidadeRepository;
	private final CadastroEstadoService cadastroEstadoService;

	public CadastroCidadeService(CidadeRepository cidadeRepository, CadastroEstadoService cadastroEstadoService) {
		this.cidadeRepository = cidadeRepository;
		this.cadastroEstadoService = cadastroEstadoService;
	}

	@Transactional
	public Cidade salvar(Cidade cidade) {
		var estado = this.cadastroEstadoService.buscarOuFalhar(cidade.getEstado().getId());
		cidade.setEstado(estado);
		return this.cidadeRepository.save(cidade);
	}

	@Transactional
	public void excluir(Long id) {
		if (!this.cidadeRepository.existsById(id)) {
			throw new CidadeNaoEncontradaException(id);
		}
		try {
			this.cidadeRepository.deleteById(id);
			this.cidadeRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_CIDADE_EM_USO, id));
		}
	}

	public Cidade buscarOuFalhar(Long cidadeId) {
		return this.cidadeRepository.findById(cidadeId).orElseThrow(() -> new CidadeNaoEncontradaException(cidadeId));
	}
}
