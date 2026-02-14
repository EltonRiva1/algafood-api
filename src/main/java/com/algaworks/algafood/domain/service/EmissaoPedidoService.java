package com.algaworks.algafood.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.repository.PedidoRepository;

@Service
public class EmissaoPedidoService {
	private final PedidoRepository pedidoRepository;
	private final CadastroRestauranteService cadastroRestauranteService;
	private final CadastroCidadeService cadastroCidadeService;
	private final CadastroUsuarioService cadastroUsuarioService;
	private final CadastroProdutoService cadastroProdutoService;
	private final CadastroFormaPagamentoService cadastroFormaPagamentoService;

	public EmissaoPedidoService(PedidoRepository pedidoRepository,
			CadastroRestauranteService cadastroRestauranteService, CadastroCidadeService cadastroCidadeService,
			CadastroUsuarioService cadastroUsuarioService, CadastroProdutoService cadastroProdutoService,
			CadastroFormaPagamentoService cadastroFormaPagamentoService) {
		this.pedidoRepository = pedidoRepository;
		this.cadastroRestauranteService = cadastroRestauranteService;
		this.cadastroCidadeService = cadastroCidadeService;
		this.cadastroUsuarioService = cadastroUsuarioService;
		this.cadastroProdutoService = cadastroProdutoService;
		this.cadastroFormaPagamentoService = cadastroFormaPagamentoService;
	}

	public Pedido buscarOuFalhar(String codigo) {
		return this.pedidoRepository.findByCodigo(codigo).orElseThrow(() -> new PedidoNaoEncontradoException(codigo));
	}

	@Transactional
	public Pedido emitir(Pedido pedido) {
		this.validarPedido(pedido);
		this.validarItens(pedido);
		pedido.setTaxaFrete(pedido.getRestaurante().getTaxaFrete());
		pedido.calcularValorTotal();
		return this.pedidoRepository.save(pedido);
	}

	private void validarPedido(Pedido pedido) {
		var cidade = this.cadastroCidadeService.buscarOuFalhar(pedido.getEnderecoEntrega().getCidade().getId());
		var cliente = this.cadastroUsuarioService.buscarOuFalhar(pedido.getCliente().getId());
		var restaurante = this.cadastroRestauranteService.buscarOuFalhar(pedido.getRestaurante().getId());
		var formaPagamento = this.cadastroFormaPagamentoService.buscarOuFalhar(pedido.getFormaPagamento().getId());
		pedido.getEnderecoEntrega().setCidade(cidade);
		pedido.setCliente(cliente);
		pedido.setRestaurante(restaurante);
		pedido.setFormaPagamento(formaPagamento);
		if (restaurante.naoAceitaFormaPagamento(formaPagamento))
			throw new NegocioException(String.format("Forma de pagamento '%s' não é aceita por esse restaurante.",
					formaPagamento.getDescricao()));
	}

	private void validarItens(Pedido pedido) {
		pedido.getItens().forEach(item -> {
			var produto = this.cadastroProdutoService.buscarOuFalhar(pedido.getRestaurante().getId(),
					item.getProduto().getId());
			item.setPedido(pedido);
			item.setProduto(produto);
			item.setPrecoUnitario(produto.getPreco());
		});
	}
}
