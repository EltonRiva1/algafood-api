package com.algaworks.algafood.domain.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.service.EnvioEmailService;

@Component
public class NotificacaoClientePedidoConfirmadoListener {
	private final EnvioEmailService envioEmailService;

	public NotificacaoClientePedidoConfirmadoListener(EnvioEmailService envioEmailService) {
		this.envioEmailService = envioEmailService;
	}

	@TransactionalEventListener
	public void aoConfirmarPedido(PedidoConfirmadoEvent pedidoConfirmadoEvent) {
		var pedido = pedidoConfirmadoEvent.pedido();
		this.envioEmailService.enviar(new EnvioEmailService.Mensagem.Builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado").corpo("pedido-confirmado.html")
				.variavel("pedido", pedido).destinatario(pedido.getCliente().getEmail()).build());
	}
}
