package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;

@Service
public class EmissaoPedidoService {
	@Autowired
	PedidoRepository pedidoRepository;
	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	@Autowired
	private CadastroProdutoService cadastroProdutoService;
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamentoService;

	@Transactional
	public Pedido emitir(Pedido pedido) {
		this.validarPedido(pedido);
		this.validarItens(pedido);
		pedido.setTaxaFrete(pedido.getRestaurante().getTaxaFrete());
		pedido.calcularValorTotal();
		return this.pedidoRepository.save(pedido);
	}

	private void validarPedido(Pedido pedido) {
		Cidade cidade = this.cadastroCidadeService.buscarOuFalhar(pedido.getEnderecoEntrega().getCidade().getId());
		Usuario cliente = this.cadastroUsuarioService.buscarOuFalhar(pedido.getCliente().getId());
		Restaurante restaurante = this.cadastroRestauranteService.buscarOuFalhar(pedido.getRestaurante().getId());
		FormaPagamento formaPagamento = this.cadastroFormaPagamentoService
				.buscarOuFalhar(pedido.getFormaPagamento().getId());
		pedido.getEnderecoEntrega().setCidade(cidade);
		pedido.setCliente(cliente);
		pedido.setRestaurante(restaurante);
		pedido.setFormaPagamento(formaPagamento);
		if (restaurante.naoAceitaFormaPagamento(formaPagamento)) {
			throw new NegocioException(String.format("Forma de pagamento '%s' não é aceita por esse restaurante.",
					formaPagamento.getDescricao()));
		}
	}

	private void validarItens(Pedido pedido) {
		pedido.getItens().forEach(item -> {
			Produto produto = this.cadastroProdutoService.buscarOuFalhar(pedido.getRestaurante().getId(),
					item.getProduto().getId());
			item.setPedido(pedido);
			item.setProduto(produto);
			item.setPrecoUnitario(produto.getPreco());
		});
	}

	public Pedido buscarOuFalhar(String codigoPedido) {
		return this.pedidoRepository.findByCodigo(codigoPedido)
				.orElseThrow(() -> new PedidoNaoEncontradoException(codigoPedido));
	}
}
