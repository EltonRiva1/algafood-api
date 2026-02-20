package com.algaworks.algafood.api.controller;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PedidoInputDisassembler;
import com.algaworks.algafood.api.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.assembler.PedidoResumoModelAssembler;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.core.data.PageWrapper;
import com.algaworks.algafood.core.data.PageableTranslator;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;
import com.algaworks.algafood.infrastructure.repository.spec.PedidoSpecs;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
	private final PedidoRepository pedidoRepository;
	private final EmissaoPedidoService emissaoPedidoService;
	private final PedidoModelAssembler pedidoModelAssembler;
	private final PedidoResumoModelAssembler pedidoResumoModelAssembler;
	private final PedidoInputDisassembler pedidoInputDisassembler;
	private final PagedResourcesAssembler<Pedido> pagedResourcesAssembler;

	public PedidoController(PedidoRepository pedidoRepository, EmissaoPedidoService emissaoPedidoService,
			PedidoModelAssembler pedidoModelAssembler, PedidoResumoModelAssembler pedidoResumoModelAssembler,
			PedidoInputDisassembler pedidoInputDisassembler, PagedResourcesAssembler<Pedido> pagedResourcesAssembler) {
		this.pedidoRepository = pedidoRepository;
		this.emissaoPedidoService = emissaoPedidoService;
		this.pedidoModelAssembler = pedidoModelAssembler;
		this.pedidoResumoModelAssembler = pedidoResumoModelAssembler;
		this.pedidoInputDisassembler = pedidoInputDisassembler;
		this.pagedResourcesAssembler = pagedResourcesAssembler;
	}

	@GetMapping
	public ResponseEntity<PagedModel<?>> pesquisar(PedidoFilter pedidoFilter, @PageableDefault() Pageable pageable) {
		var pedidosPage = this.pedidoRepository.findAll(PedidoSpecs.usandoFiltro(pedidoFilter),
				this.traduzirPageable(pageable));
		pedidosPage = new PageWrapper<>(pedidosPage, pageable);
		return ResponseEntity.ok(this.pagedResourcesAssembler.toModel(pedidosPage, this.pedidoResumoModelAssembler));
	}

	@GetMapping("/{codigoPedido}")
	public ResponseEntity<?> buscar(@PathVariable String codigoPedido) {
		return ResponseEntity
				.ok(this.pedidoModelAssembler.toModel(this.emissaoPedidoService.buscarOuFalhar(codigoPedido)));
	}

	@PostMapping
	public ResponseEntity<?> adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
		try {
			var pedido = this.pedidoInputDisassembler.toDomainObject(pedidoInput);
			pedido.setCliente(new Usuario());
			pedido.getCliente().setId(1L);
			pedido = this.emissaoPedidoService.emitir(pedido);
			return ResponseEntity.status(HttpStatus.CREATED).body(this.pedidoModelAssembler.toModel(pedido));
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	private Pageable traduzirPageable(Pageable pageable) {
		return PageableTranslator.translate(pageable,
				Map.of("codigo", "codigo", "subtotal", "subtotal", "taxaFrete", "taxaFrete", "restaurante.nome",
						"restaurante.nome", "cliente.nome", "cliente.nome", "valorTotal", "valorTotal", "dataCriacao",
						"dataCriacao", "restaurante.id", "restaurante.id", "cliente.id", "cliente.id"));
	}
}
