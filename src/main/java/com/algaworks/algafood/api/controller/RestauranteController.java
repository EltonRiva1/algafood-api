package com.algaworks.algafood.api.controller;

import java.util.List;

import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.api.model.view.RestauranteView;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {
	private final RestauranteRepository restauranteRepository;
	private final CadastroRestauranteService cadastroRestauranteService;
	private final RestauranteModelAssembler restauranteModelAssembler;
	private final RestauranteInputDisassembler restauranteInputDisassembler;

	public RestauranteController(RestauranteRepository restauranteRepository,
			CadastroRestauranteService cadastroRestauranteService, RestauranteModelAssembler restauranteModelAssembler,
			RestauranteInputDisassembler restauranteInputDisassembler) {
		this.restauranteRepository = restauranteRepository;
		this.cadastroRestauranteService = cadastroRestauranteService;
		this.restauranteModelAssembler = restauranteModelAssembler;
		this.restauranteInputDisassembler = restauranteInputDisassembler;
	}

	@GetMapping
	@JsonView(RestauranteView.Resumo.class)
	public ResponseEntity<List<?>> listar() {
		return ResponseEntity.ok(this.restauranteModelAssembler
				.toCollectionModel(this.restauranteRepository.findAllFetchingEnderecoCidade()));
	}

	@GetMapping("/{restauranteId}")
	public ResponseEntity<?> buscar(@PathVariable Long restauranteId) {
		return ResponseEntity.ok(
				this.restauranteModelAssembler.toModel(this.cadastroRestauranteService.buscarOuFalhar(restauranteId)));
	}

	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(this.restauranteModelAssembler.toModel(this.cadastroRestauranteService
							.salvar(this.restauranteInputDisassembler.toDomainObject(restauranteInput))));
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}")
	public ResponseEntity<?> atualizar(@PathVariable Long restauranteId,
			@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			var restauranteAtual = this.cadastroRestauranteService.buscarOuFalhar(restauranteId);
			this.restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
			return ResponseEntity.ok(
					this.restauranteModelAssembler.toModel(this.cadastroRestauranteService.salvar(restauranteAtual)));
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}/ativo")
	public ResponseEntity<?> ativar(@PathVariable Long restauranteId) {
		this.cadastroRestauranteService.ativar(restauranteId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{restauranteId}/ativo")
	public ResponseEntity<?> inativar(@PathVariable Long restauranteId) {
		this.cadastroRestauranteService.inativar(restauranteId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{restauranteId}/abertura")
	public ResponseEntity<?> abrir(@PathVariable Long restauranteId) {
		this.cadastroRestauranteService.abrir(restauranteId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{restauranteId}/fechamento")
	public ResponseEntity<?> fechar(@PathVariable Long restauranteId) {
		this.cadastroRestauranteService.fechar(restauranteId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/ativacoes")
	public ResponseEntity<?> ativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			this.cadastroRestauranteService.ativar(restauranteIds);
			return ResponseEntity.noContent().build();
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@DeleteMapping("/ativacoes")
	public ResponseEntity<?> inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			this.cadastroRestauranteService.inativar(restauranteIds);
			return ResponseEntity.noContent().build();
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@GetMapping(params = "projecao=apenas-nome")
	@JsonView(RestauranteView.ApenasNome.class)
	public ResponseEntity<List<?>> listarApenasNome() {
		return this.listar();
	}
}
