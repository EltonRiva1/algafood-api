package com.algaworks.algafood.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.repository.PedidoRepository;

@Service
public class FluxoPedidoService {
	private final EmissaoPedidoService emissaoPedidoService;
	private final PedidoRepository pedidoRepository;

	public FluxoPedidoService(EmissaoPedidoService emissaoPedidoService, PedidoRepository pedidoRepository) {
		this.emissaoPedidoService = emissaoPedidoService;
		this.pedidoRepository = pedidoRepository;
	}

	@Transactional
	public void confirmar(String codigoPedido) {
		var pedido = this.emissaoPedidoService.buscarOuFalhar(codigoPedido);
		pedido.confirmar();
		this.pedidoRepository.save(pedido);
	}

	@Transactional
	public void cancelar(String codigoPedido) {
		var pedido = this.emissaoPedidoService.buscarOuFalhar(codigoPedido);
		pedido.cancelar();
		this.pedidoRepository.save(pedido);
	}

	@Transactional
	public void entregar(String codigoPedido) {
		var pedido = this.emissaoPedidoService.buscarOuFalhar(codigoPedido);
		pedido.entregar();
	}
}
