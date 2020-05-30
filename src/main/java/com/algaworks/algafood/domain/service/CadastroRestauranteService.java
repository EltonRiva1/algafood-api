package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {
	@Autowired
	private RestauranteRepository restauranteRepository;
	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;
	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamentoService;
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;

	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId(), cidadeId = restaurante.getEndereco().getCidade().getId();
		Cozinha cozinha = this.cadastroCozinhaService.buscarOuFalhar(cozinhaId);
		Cidade cidade = this.cadastroCidadeService.buscarOuFalhar(cidadeId);
		restaurante.setCozinha(cozinha);
		restaurante.getEndereco().setCidade(cidade);
		return this.restauranteRepository.save(restaurante);
	}

	@Transactional
	public void ativar(Long restauranteId) {
		Restaurante restauranteAtual = this.buscarOuFalhar(restauranteId);
		restauranteAtual.ativar();
	}

	@Transactional
	public void inativar(Long restauranteId) {
		Restaurante restauranteAtual = this.buscarOuFalhar(restauranteId);
		restauranteAtual.inativar();
	}

	@Transactional
	public void ativar(List<Long> restauranteIds) {
		restauranteIds.forEach(this::ativar);
	}

	@Transactional
	public void inativar(List<Long> restauranteIds) {
		restauranteIds.forEach(this::inativar);
	}

	@Transactional
	public void abrir(Long restauranteId) {
		Restaurante restauranteAtual = this.buscarOuFalhar(restauranteId);
		restauranteAtual.abrir();
	}

	@Transactional
	public void fechar(Long restauranteId) {
		Restaurante restauranteAtual = this.buscarOuFalhar(restauranteId);
		restauranteAtual.fechar();
	}

	@Transactional
	public void desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = this.buscarOuFalhar(restauranteId);
		FormaPagamento formaPagamento = this.cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
		restaurante.removerFormaPagamento(formaPagamento);
	}

	@Transactional
	public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = this.buscarOuFalhar(restauranteId);
		FormaPagamento formaPagamento = this.cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
		restaurante.adicionarFormaPagamento(formaPagamento);
	}

	@Transactional
	public void desassociarResponsavel(Long restauranteId, Long usuarioId) {
		Restaurante restaurante = this.buscarOuFalhar(restauranteId);
		Usuario usuario = this.cadastroUsuarioService.buscarOuFalhar(usuarioId);
		restaurante.removerResponsavel(usuario);
	}

	@Transactional
	public void associarResponsavel(Long restauranteId, Long usuarioId) {
		Restaurante restaurante = this.buscarOuFalhar(restauranteId);
		Usuario usuario = this.cadastroUsuarioService.buscarOuFalhar(usuarioId);
		restaurante.adicionarResponsavel(usuario);
	}

	public Restaurante buscarOuFalhar(Long restauranteId) {
		return this.restauranteRepository.findById(restauranteId)
				.orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
	}
}
