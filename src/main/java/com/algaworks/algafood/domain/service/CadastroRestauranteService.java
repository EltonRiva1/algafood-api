package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroRestauranteService {
	private final RestauranteRepository restauranteRepository;
	private final CadastroCozinhaService cadastroCozinhaService;
	private final CadastroCidadeService cadastroCidadeService;
	private final CadastroFormaPagamentoService cadastroFormaPagamentoService;
	private final CadastroUsuarioService cadastroUsuarioService;

	public CadastroRestauranteService(RestauranteRepository restauranteRepository,
			CadastroCozinhaService cadastroCozinhaService, CadastroCidadeService cadastroCidadeService,
			CadastroFormaPagamentoService cadastroFormaPagamentoService,
			CadastroUsuarioService cadastroUsuarioService) {
		this.restauranteRepository = restauranteRepository;
		this.cadastroCozinhaService = cadastroCozinhaService;
		this.cadastroCidadeService = cadastroCidadeService;
		this.cadastroFormaPagamentoService = cadastroFormaPagamentoService;
		this.cadastroUsuarioService = cadastroUsuarioService;
	}

	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		var cozinha = this.cadastroCozinhaService.buscarOuFalhar(restaurante.getCozinha().getId());
		var cidade = this.cadastroCidadeService.buscarOuFalhar(restaurante.getEndereco().getCidade().getId());
		restaurante.setCozinha(cozinha);
		restaurante.getEndereco().setCidade(cidade);
		restaurante = this.restauranteRepository.save(restaurante);
		var finalRestaurante = restaurante;
		return this.restauranteRepository.findByIdFetchingEnderecoCidade(restaurante.getId())
				.orElseThrow(() -> new RestauranteNaoEncontradoException(finalRestaurante.getId()));
	}

	@Transactional(readOnly = true)
	public Restaurante buscarOuFalhar(Long restauranteId) {
		return this.restauranteRepository.findByIdFetchingEnderecoCidade(restauranteId)
				.orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
	}

	@Transactional
	public void ativar(Long restauranteId) {
		var restaurante = this.buscarOuFalhar(restauranteId);
		restaurante.ativar();
	}

	@Transactional
	public void inativar(Long restauranteId) {
		var restaurante = this.buscarOuFalhar(restauranteId);
		restaurante.inativar();
	}

	@Transactional(readOnly = true)
	public Restaurante buscarOuFalharComFormasPagamento(Long restauranteId) {
		return this.restauranteRepository.findByIdFetchingFormasPagamento(restauranteId)
				.orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
	}

	@Transactional
	public void desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		var restaurante = this.buscarOuFalhar(restauranteId);
		var formaPagamento = this.cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
		restaurante.removerFormaPagamento(formaPagamento);
	}

	@Transactional
	public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		var restaurante = this.buscarOuFalhar(restauranteId);
		var formaPagamento = this.cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
		restaurante.adicionarFormaPagamento(formaPagamento);
	}

	@Transactional
	public void abrir(Long restauranteId) {
		var restaurante = this.buscarOuFalhar(restauranteId);
		restaurante.abrir();
	}

	@Transactional
	public void fechar(Long restauranteId) {
		var restaurante = this.buscarOuFalhar(restauranteId);
		restaurante.fechar();
	}

	@Transactional
	public void desassociarResponsavel(Long restauranteId, Long usuarioId) {
		var restaurante = this.buscarOuFalhar(restauranteId);
		var usuario = this.cadastroUsuarioService.buscarOuFalhar(usuarioId);
		restaurante.removerResponsavel(usuario);

	}

	@Transactional
	public void associarResponsavel(Long restauranteId, Long usuarioId) {
		var restaurante = this.buscarOuFalhar(restauranteId);
		var usuario = this.cadastroUsuarioService.buscarOuFalhar(usuarioId);
		restaurante.adicionarResponsavel(usuario);
	}

	@Transactional(readOnly = true)
	public Restaurante buscarOuFalharComResponsaveis(Long restauranteId) {
		return this.restauranteRepository.findByIdFetchingResponsaveis(restauranteId)
				.orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
	}

	@Transactional
	public void ativar(List<Long> restauranteIds) {
		restauranteIds.forEach(this::ativar);
	}

	@Transactional
	public void inativar(List<Long> restauranteIds) {
		restauranteIds.forEach(this::inativar);
	}
}
