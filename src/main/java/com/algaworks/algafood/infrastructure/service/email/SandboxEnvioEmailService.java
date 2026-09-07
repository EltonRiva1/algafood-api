package com.algaworks.algafood.infrastructure.service.email;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.service.EnvioEmailService;

public record SandboxEnvioEmailService(EnvioEmailService envioEmailService, EmailProperties emailProperties)
		implements EnvioEmailService {

	@Override
	public void enviar(Mensagem mensagem) {
		this.envioEmailService.enviar(new Mensagem.Builder()
				.destinatario(this.emailProperties.getSandbox().getDestinatario()).assunto(mensagem.getAssunto())
				.corpo(mensagem.getCorpo()).variaveis(mensagem.getVariaveis()).build());
	}

}
