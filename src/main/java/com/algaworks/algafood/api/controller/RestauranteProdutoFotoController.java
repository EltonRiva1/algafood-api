package com.algaworks.algafood.api.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.FotoProdutoModelAssembler;
import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CatalogoFotoProdutoService;
import com.algaworks.algafood.domain.service.FotoStorageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {
	private final CatalogoFotoProdutoService catalogoFotoProdutoService;
	private final CadastroProdutoService cadastroProdutoService;
	private final FotoProdutoModelAssembler fotoProdutoModelAssembler;
	private final FotoStorageService fotoStorageService;

	public RestauranteProdutoFotoController(CatalogoFotoProdutoService catalogoFotoProdutoService,
			CadastroProdutoService cadastroProdutoService, FotoProdutoModelAssembler fotoProdutoModelAssembler,
			FotoStorageService fotoStorageService) {
		this.catalogoFotoProdutoService = catalogoFotoProdutoService;
		this.cadastroProdutoService = cadastroProdutoService;
		this.fotoProdutoModelAssembler = fotoProdutoModelAssembler;
		this.fotoStorageService = fotoStorageService;
	}

	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId,
			@Valid FotoProdutoInput fotoProdutoInput) throws IOException {
		var fotoProduto = new FotoProduto();
		fotoProduto.setProduto(this.cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId));
		fotoProduto.setDescricao(fotoProdutoInput.getDescricao());
		fotoProduto.setContentType(fotoProdutoInput.getMultipartFile().getContentType());
		fotoProduto.setTamanho(fotoProdutoInput.getMultipartFile().getSize());
		fotoProduto.setNomeArquivo(fotoProdutoInput.getMultipartFile().getOriginalFilename());
		return ResponseEntity.ok(this.fotoProdutoModelAssembler.toModel(this.catalogoFotoProdutoService
				.salvar(fotoProduto, fotoProdutoInput.getMultipartFile().getInputStream())));
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		return ResponseEntity.ok(this.fotoProdutoModelAssembler
				.toModel(this.catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId)));
	}

	@GetMapping
	public ResponseEntity<?> servirFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId,
			@RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {
		try {
			var fotoProduto = this.catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);
			var mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
			this.verificarCompatibilidadeMediaType(mediaTypeFoto, MediaType.parseMediaTypes(acceptHeader));
			return ResponseEntity.ok().contentType(mediaTypeFoto).body(new InputStreamResource(
					this.fotoStorageService.recuperar(fotoProduto.getNomeArquivo()).getInputStream()));
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}

	private void verificarCompatibilidadeMediaType(MediaType mediaType, List<MediaType> mediaTypes)
			throws HttpMediaTypeNotAcceptableException {
		boolean compativel = mediaTypes.stream()
				.anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaType));
		if (!compativel)
			throw new HttpMediaTypeNotAcceptableException(mediaTypes);
	}

	@DeleteMapping
	public ResponseEntity<?> excluir(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		this.catalogoFotoProdutoService.excluir(restauranteId, produtoId);
		return ResponseEntity.noContent().build();
	}
}
