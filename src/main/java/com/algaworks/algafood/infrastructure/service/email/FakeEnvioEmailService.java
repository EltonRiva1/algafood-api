package com.algaworks.algafood.infrastructure.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.algaworks.algafood.domain.service.EnvioEmailService;

public class FakeEnvioEmailService implements EnvioEmailService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FakeEnvioEmailService.class);
	private final ProcessadorEmailTemplate processadorEmailTemplate;

	public FakeEnvioEmailService(ProcessadorEmailTemplate processadorEmailTemplate) {
		this.processadorEmailTemplate = processadorEmailTemplate;
	}

	@Override
	public void enviar(Mensagem mensagem) {
		LOGGER.info("[FAKE E-MAIL] Para: {}\n{}", mensagem.getDestinatarios(),
				this.processadorEmailTemplate.processar(mensagem));
	}
}
