package com.algaworks.algafood.infrastructure.service.email;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.algaworks.algafood.domain.service.EnvioEmailService.Mensagem;

import freemarker.template.Configuration;

public record ProcessadorEmailTemplate(Configuration configuration) {

	public String processar(Mensagem mensagem) {
		try {
			return FreeMarkerTemplateUtils.processTemplateIntoString(
					this.configuration.getTemplate(mensagem.getCorpo()), mensagem.getVariaveis());
		} catch (Exception e) {
			throw new EmailException("Não foi possível montar o template do e-mail", e);
		}
	}
}
