package com.algaworks.algafood.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.service.FluxoPedidoService;

@RestController
@RequestMapping("/pedidos/{codigoPedido}")
public class FluxoPedidoController {
	private final FluxoPedidoService fluxoPedidoService;

	public FluxoPedidoController(FluxoPedidoService fluxoPedidoService) {
		this.fluxoPedidoService = fluxoPedidoService;
	}

	@PutMapping("/confirmacao")
	public ResponseEntity<?> confirmar(@PathVariable String codigoPedido) {
		this.fluxoPedidoService.confirmar(codigoPedido);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/cancelamento")
	public ResponseEntity<?> cancelar(@PathVariable String codigoPedido) {
		this.fluxoPedidoService.cancelar(codigoPedido);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/entrega")
	public ResponseEntity<?> entregar(@PathVariable String codigoPedido) {
		this.fluxoPedidoService.entregar(codigoPedido);
		return ResponseEntity.noContent().build();
	}
}
