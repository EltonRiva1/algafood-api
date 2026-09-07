package com.algaworks.algafood;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.ActiveProfiles;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DataBaseCleaner;
import com.algaworks.algafood.util.OAuth2TestUtils;
import com.algaworks.algafood.util.ResourceUtils;
import com.algaworks.algafood.util.TestSecurityConfig;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class CadastroCozinhaIT {
	private static final int COZINHA_ID_INEXISTENTE = 100;
	@LocalServerPort
	private int port;
	@Autowired
	private DataBaseCleaner baseCleaner;
	@Autowired
	private CozinhaRepository cozinhaRepository;
	@Autowired
	private JwtEncoder jwtEncoder;
	private Cozinha cozinhaAmericana;
	private int quantidadeCozinhasCadastradas;
	private String jsonCorretoCozinhaChinesa;

	@BeforeEach
	void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/v1/cozinhas";
		jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource("/json/correto/cozinha-chinesa.json");
		baseCleaner.clearTables();
		prepararDados();
	}

	@Test
	void deveRetornarStatus200_QuandoConsultarCozinhas() {
		RestAssured.given().header(HttpHeaders.AUTHORIZATION, bearerTokenLeitura()).accept(ContentType.JSON).when()
				.get().then().statusCode(HttpStatus.OK.value());
	}

	@Test
	void deveRetornarQuantidadeCorretaDeCozinhas_QuandoConsultarCozinhas() {
		RestAssured.given().header(HttpHeaders.AUTHORIZATION, bearerTokenLeitura()).accept(ContentType.JSON).when()
				.get().then().body("_embedded.cozinhas", Matchers.hasSize(quantidadeCozinhasCadastradas));
	}

	@Test
	void deveRetornarStatus201_QuandoCadastrarCozinha() {
		RestAssured.given().header(HttpHeaders.AUTHORIZATION, bearerTokenEdicao()).body(jsonCorretoCozinhaChinesa)
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().post().then()
				.statusCode(HttpStatus.CREATED.value());
	}

	@Test
	void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
		RestAssured.given().header(HttpHeaders.AUTHORIZATION, bearerTokenLeitura())
				.pathParam("cozinhaId", cozinhaAmericana.getId()).accept(ContentType.JSON).when().get("/{cozinhaId}")
				.then().statusCode(HttpStatus.OK.value()).body("nome", equalTo(cozinhaAmericana.getNome()));
	}

	@Test
	void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {
		RestAssured.given().header(HttpHeaders.AUTHORIZATION, bearerTokenLeitura())
				.pathParam("cozinhaId", COZINHA_ID_INEXISTENTE).accept(ContentType.JSON).when().get("/{cozinhaId}")
				.then().statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	void deveRetornarStatus401_QuandoConsultarCozinhasSemToken() {
		RestAssured.given().accept(ContentType.JSON).when().get().then().statusCode(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	void deveRetornarStatus403_QuandoCadastrarCozinhaSemPermissao() {
		String token = OAuth2TestUtils.gerarToken(jwtEncoder, List.of("WRITE"));
		RestAssured.given().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).body(jsonCorretoCozinhaChinesa)
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().post().then()
				.statusCode(HttpStatus.FORBIDDEN.value());
	}

	private String bearerTokenLeitura() {
		return "Bearer " + OAuth2TestUtils.gerarToken(jwtEncoder, List.of("READ"));
	}

	private String bearerTokenEdicao() {
		return "Bearer " + OAuth2TestUtils.gerarToken(jwtEncoder, List.of("WRITE"), "EDITAR_COZINHAS");
	}

	private void prepararDados() {
		var cozinhaTailandesa = new Cozinha();
		cozinhaTailandesa.setNome("Tailandesa");
		cozinhaRepository.save(cozinhaTailandesa);
		cozinhaAmericana = new Cozinha();
		cozinhaAmericana.setNome("Americana");
		cozinhaRepository.save(cozinhaAmericana);
		quantidadeCozinhasCadastradas = (int) cozinhaRepository.count();
	}
}
