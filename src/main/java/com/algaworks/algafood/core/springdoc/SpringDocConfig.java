package com.algaworks.algafood.core.springdoc;

import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.core.security.authorizationserver.AuthorizationServerProperties;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocConfig {
	public static final String SECURITY_SCHEME_NAME = "security_auth";
	private final AuthorizationServerProperties authorizationServerProperties;

	public SpringDocConfig(AuthorizationServerProperties authorizationServerProperties) {
		this.authorizationServerProperties = authorizationServerProperties;
	}

	@Bean
	OpenAPI openAPI() {
		var issuer = authorizationServerProperties.issuer();
		var scopes = new Scopes().addString("READ", "Acesso de leitura").addString("WRITE", "Acesso de escrita");
		var authorizationCode = new OAuthFlow().authorizationUrl(issuer + "/oauth2/authorize")
				.tokenUrl(issuer + "/oauth2/token").scopes(scopes);
		var securityScheme = new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
				.flows(new OAuthFlows().authorizationCode(authorizationCode));
		return new OpenAPI()
				.info(new Info().title("AlgaFood API").version("v1").description("REST API do AlgaFood")
						.license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
						.contact(new Contact().name("Elton Riva").url("https://github.com/EltonRiva1")))
				.externalDocs(new ExternalDocumentation().description("AlgaWorks").url("https://algaworks.com"))
				.components(new Components().schemas(gerarSchemas()).addSecuritySchemes(SECURITY_SCHEME_NAME,
						securityScheme));
	}

	@Bean
	GroupedOpenApi algaFoodOpenApiV1(OpenApiCustomizer openApiCustomizer) {
		return GroupedOpenApi.builder().group("AlgaFood API v1").pathsToMatch("/v1/**")
				.addOpenApiCustomizer(openApiCustomizer).build();
	}

	@Bean
	GroupedOpenApi algaFoodOpenApiV2(OpenApiCustomizer openApiCustomizer) {
		return GroupedOpenApi.builder().group("AlgaFood API v2").pathsToMatch("/v2/**")
				.addOpenApiCustomizer(openApiCustomizer).build();
	}

	@Bean
	OpenApiCustomizer openApiCustomizer() {
		return openApi -> openApi.getPaths().values()
				.forEach(pathItem -> pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
					var responses = operation.getResponses();
					switch (httpMethod) {
					case GET -> {
						addIfAbsent(responses, "404", problemResponse("Recurso não encontrado"));
						addIfAbsent(responses, "406", problemResponse("Recurso não possui representação aceitável"));
					}
					case POST -> addIfAbsent(responses, "400", problemResponse("Requisição inválida"));
					case PUT -> {
						addIfAbsent(responses, "400", problemResponse("Requisição inválida"));
						addIfAbsent(responses, "404", problemResponse("Recurso não encontrado"));
					}
					case DELETE -> addIfAbsent(responses, "404", problemResponse("Recurso não encontrado"));
					default -> {
					}
					}
					addIfAbsent(responses, "500", problemResponse("Erro interno no servidor"));
				}));
	}

	private void addIfAbsent(ApiResponses responses, String status, ApiResponse response) {
		if (!responses.containsKey(status)) {
			responses.addApiResponse(status, response);
		}
	}

	private ApiResponse problemResponse(String description) {
		var mediaType = new MediaType().schema(new Schema<>().$ref("#/components/schemas/Problema"));
		return new ApiResponse().description(description).content(
				new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType));
	}

	@SuppressWarnings("rawtypes")
	private Map<String, Schema> gerarSchemas() {
		var schemas = new HashMap<String, Schema>();
		schemas.putAll(ModelConverters.getInstance().read(Problem.class));
		schemas.putAll(ModelConverters.getInstance().read(Problem.Object.class));
		return schemas;
	}
}