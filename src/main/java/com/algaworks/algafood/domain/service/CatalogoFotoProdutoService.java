package com.algaworks.algafood.domain.service;

import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.FotoProdutoNaoEncontradaException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;

@Service
public class CatalogoFotoProdutoService {
	private final ProdutoRepository produtoRepository;
	private final FotoStorageService fotoStorageService;

	public CatalogoFotoProdutoService(ProdutoRepository produtoRepository, FotoStorageService fotoStorageService) {
		this.produtoRepository = produtoRepository;
		this.fotoStorageService = fotoStorageService;
	}

	@Transactional
	public FotoProduto salvar(FotoProduto fotoProduto, InputStream inputStream) {
		String nomeArquivoExistente = null;
		var fotoExistente = this.produtoRepository.findFotoById(fotoProduto.getRestauranteId(),
				fotoProduto.getProduto().getId());
		if (fotoExistente.isPresent()) {
			nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
			this.produtoRepository.delete(fotoExistente.get());
		}
		fotoProduto.setNomeArquivo(this.fotoStorageService.gerarNomeArquivo(fotoProduto.getNomeArquivo()));
		fotoProduto = this.produtoRepository.save(fotoProduto);
		this.produtoRepository.flush();
		this.fotoStorageService.substituir(nomeArquivoExistente, new FotoStorageService.NovaFoto.Builder()
				.nomeArquivo(fotoProduto.getNomeArquivo()).inputStream(inputStream).build());
		return fotoProduto;
	}

	public FotoProduto buscarOuFalhar(Long restauranteId, Long produtoId) {
		return this.produtoRepository.findFotoById(restauranteId, produtoId)
				.orElseThrow(() -> new FotoProdutoNaoEncontradaException(restauranteId, produtoId));
	}

	@Transactional
	public void excluir(Long restauranteId, Long produtoId) {
		var foto = this.buscarOuFalhar(restauranteId, produtoId);
		this.produtoRepository.delete(foto);
		this.produtoRepository.flush();
		this.fotoStorageService.remover(foto.getNomeArquivo());
	}
}
