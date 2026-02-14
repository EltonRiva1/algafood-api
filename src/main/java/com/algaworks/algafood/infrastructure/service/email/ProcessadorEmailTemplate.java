package com.algaworks.algafood.infrastructure.service.email;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.algaworks.algafood.domain.service.EnvioEmailService.Mensagem;

import freemarker.template.Configuration;

public class ProcessadorEmailTemplate {
	private final Configuration configuration;

	public ProcessadorEmailTemplate(Configuration configuration) {
		this.configuration = configuration;
	}

	public String processar(Mensagem mensagem) {
		try {
			return FreeMarkerTemplateUtils.processTemplateIntoString(
					this.configuration.getTemplate(mensagem.getCorpo()), mensagem.getVariaveis());
		} catch (Exception e) {
			throw new EmailException("Não foi possível montar o template do e-mail", e);
		}
	}
}
