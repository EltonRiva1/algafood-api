package com.algaworks.algafood.core.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import com.algaworks.algafood.domain.service.EnvioEmailService;
import com.algaworks.algafood.infrastructure.service.email.FakeEnvioEmailService;
import com.algaworks.algafood.infrastructure.service.email.ProcessadorEmailTemplate;
import com.algaworks.algafood.infrastructure.service.email.SandboxEnvioEmailService;
import com.algaworks.algafood.infrastructure.service.email.SmtpEnvioEmailService;

@Configuration
public class EmailConfig {
	private final EmailProperties emailProperties;

	public EmailConfig(EmailProperties emailProperties) {
		this.emailProperties = emailProperties;
	}

	@Bean
	ProcessadorEmailTemplate processadorEmailTemplate(freemarker.template.Configuration configuration) {
		return new ProcessadorEmailTemplate(configuration);
	}

	@Bean
	EnvioEmailService envioEmailService(JavaMailSender javaMailSender,
			ProcessadorEmailTemplate processadorEmailTemplate) {
		var smtp = new SmtpEnvioEmailService(javaMailSender, this.emailProperties, processadorEmailTemplate);
		return switch (this.emailProperties.getImpl()) {
		case FAKE -> new FakeEnvioEmailService(processadorEmailTemplate);
		case SMTP -> smtp;
		case SANDBOX -> new SandboxEnvioEmailService(smtp, this.emailProperties);
		};
	}
}
