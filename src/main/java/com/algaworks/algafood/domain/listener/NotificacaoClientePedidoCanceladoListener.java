package com.algaworks.algafood.domain.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.algaworks.algafood.domain.event.PedidoCanceladoEvent;
import com.algaworks.algafood.domain.service.EnvioEmailService;

@Component
public class NotificacaoClientePedidoCanceladoListener {
	private final EnvioEmailService envioEmailService;

	public NotificacaoClientePedidoCanceladoListener(EnvioEmailService envioEmailService) {
		this.envioEmailService = envioEmailService;
	}

	@TransactionalEventListener
	public void aoCancelarPedido(PedidoCanceladoEvent pedidoCanceladoEvent) {
		var pedido = pedidoCanceladoEvent.pedido();
		this.envioEmailService.enviar(new EnvioEmailService.Mensagem.Builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido cancelado").corpo("pedido-cancelado.html")
				.variavel("pedido", pedido).destinatario(pedido.getCliente().getEmail()).build());
	}
}
