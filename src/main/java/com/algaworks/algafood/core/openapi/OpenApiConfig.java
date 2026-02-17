package com.algaworks.algafood.core.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("AlgaFood API").version("1.0")
				.description("API de delivery de comida").contact(new Contact().name("Elton Riva")
						.url("https://github.com/EltonRiva1").email("notleamil@hotmail.com")));
	}
}
