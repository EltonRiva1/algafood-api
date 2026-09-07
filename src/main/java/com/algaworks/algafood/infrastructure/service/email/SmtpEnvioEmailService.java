package com.algaworks.algafood.infrastructure.service.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.service.EnvioEmailService;

public record SmtpEnvioEmailService(JavaMailSender javaMailSender, EmailProperties emailProperties,
		ProcessadorEmailTemplate processadorEmailTemplate) implements EnvioEmailService {

	@Override
	public void enviar(Mensagem mensagem) {
		try {
			var mimeMessage = this.javaMailSender.createMimeMessage();
			var mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
			mimeMessageHelper.setSubject(mensagem.getAssunto());
			mimeMessageHelper.setText(this.processadorEmailTemplate.processar(mensagem), true);
			mimeMessageHelper.setTo(mensagem.getDestinatarios().toArray(new String[0]));
			mimeMessageHelper.setFrom(this.emailProperties.getRemetente());
			this.javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new EmailException("Não foi possível enviar e-mail", e);
		}
	}
}
