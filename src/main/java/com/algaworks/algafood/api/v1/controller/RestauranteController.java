package com.algaworks.algafood.api.v1.controller;

import java.util.List;

import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.RestauranteApenasNomeModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteBasicoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.v1.model.input.RestauranteInput;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping(value = "/v1/restaurantes")
public class RestauranteController {
	private final RestauranteRepository restauranteRepository;
	private final CadastroRestauranteService cadastroRestauranteService;
	private final RestauranteModelAssembler restauranteModelAssembler;
	private final RestauranteInputDisassembler restauranteInputDisassembler;
	private final RestauranteBasicoModelAssembler restauranteBasicoModelAssembler;
	private final RestauranteApenasNomeModelAssembler restauranteApenasNomeModelAssembler;

	public RestauranteController(RestauranteRepository restauranteRepository,
			CadastroRestauranteService cadastroRestauranteService, RestauranteModelAssembler restauranteModelAssembler,
			RestauranteInputDisassembler restauranteInputDisassembler,
			RestauranteBasicoModelAssembler restauranteBasicoModelAssembler,
			RestauranteApenasNomeModelAssembler restauranteApenasNomeModelAssembler) {
		this.restauranteRepository = restauranteRepository;
		this.cadastroRestauranteService = cadastroRestauranteService;
		this.restauranteModelAssembler = restauranteModelAssembler;
		this.restauranteInputDisassembler = restauranteInputDisassembler;
		this.restauranteBasicoModelAssembler = restauranteBasicoModelAssembler;
		this.restauranteApenasNomeModelAssembler = restauranteApenasNomeModelAssembler;
	}

	@GetMapping
	@CheckSecurity.Restaurantes.PodeConsultar
	public ResponseEntity<CollectionModel<?>> listar(
			@Parameter(in = ParameterIn.QUERY, name = "projecao", description = "Nome da projeção de pedidos", schema = @Schema(allowableValues = {
					"apenas-nome" })) @RequestParam(required = false) String projecao) {
		return ResponseEntity.ok(this.restauranteBasicoModelAssembler
				.toCollectionModel(this.restauranteRepository.findAllFetchingEnderecoCidade()));
	}

	@GetMapping("/{restauranteId}")
	@CheckSecurity.Restaurantes.PodeConsultar
	public ResponseEntity<?> buscar(@PathVariable Long restauranteId) {
		return ResponseEntity.ok(
				this.restauranteModelAssembler.toModel(this.cadastroRestauranteService.buscarOuFalhar(restauranteId)));
	}

	@PostMapping
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
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
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
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
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	public ResponseEntity<?> ativar(@PathVariable Long restauranteId) {
		this.cadastroRestauranteService.ativar(restauranteId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{restauranteId}/ativo")
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	public ResponseEntity<?> inativar(@PathVariable Long restauranteId) {
		this.cadastroRestauranteService.inativar(restauranteId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{restauranteId}/abertura")
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	public ResponseEntity<?> abrir(@PathVariable Long restauranteId) {
		this.cadastroRestauranteService.abrir(restauranteId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{restauranteId}/fechamento")
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	public ResponseEntity<?> fechar(@PathVariable Long restauranteId) {
		this.cadastroRestauranteService.fechar(restauranteId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/ativacoes")
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	public ResponseEntity<?> ativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			this.cadastroRestauranteService.ativar(restauranteIds);
			return ResponseEntity.noContent().build();
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@DeleteMapping("/ativacoes")
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	public ResponseEntity<?> inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			this.cadastroRestauranteService.inativar(restauranteIds);
			return ResponseEntity.noContent().build();
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@GetMapping(params = "projecao=apenas-nome")
	@CheckSecurity.Restaurantes.PodeConsultar
	public ResponseEntity<CollectionModel<?>> listarApenasNome() {
		return ResponseEntity.ok(this.restauranteApenasNomeModelAssembler
				.toCollectionModel(this.restauranteRepository.findAllFetchingEnderecoCidade()));
	}
}
