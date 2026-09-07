package com.algaworks.algafood;

import static org.hamcrest.CoreMatchers.equalTo;

import java.math.BigDecimal;
import java.util.List;

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

import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.util.DataBaseCleaner;
import com.algaworks.algafood.util.OAuth2TestUtils;
import com.algaworks.algafood.util.ResourceUtils;
import com.algaworks.algafood.util.TestSecurityConfig;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class CadastroRestauranteIT {
	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";
	private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
	private static final int RESTAURANTE_ID_INEXISTENTE = 100;
	@LocalServerPort
	private int port;
	@Autowired
	private DataBaseCleaner databaseCleaner;
	@Autowired
	private CozinhaRepository cozinhaRepository;
	@Autowired
	private RestauranteRepository restauranteRepository;
	@Autowired
	private JwtEncoder jwtEncoder;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	private String jsonRestauranteCorreto;
	private String jsonRestauranteSemFrete;
	private String jsonRestauranteSemCozinha;
	private String jsonRestauranteComCozinhaInexistente;
	private Restaurante burgerTopRestaurante;
	private Cidade cidadeSaoPaulo;

	@BeforeEach
	void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/v1/restaurantes";
		jsonRestauranteCorreto = ResourceUtils
				.getContentFromResource("/json/correto/restaurante-new-york-barbecue.json");
		jsonRestauranteSemFrete = ResourceUtils
				.getContentFromResource("/json/incorreto/restaurante-new-york-barbecue-sem-frete.json");
		jsonRestauranteSemCozinha = ResourceUtils
				.getContentFromResource("/json/incorreto/restaurante-new-york-barbecue-sem-cozinha.json");
		jsonRestauranteComCozinhaInexistente = ResourceUtils
				.getContentFromResource("/json/incorreto/restaurante-new-york-barbecue-com-cozinha-inexistente.json");
		databaseCleaner.clearTables();
		prepararDados();
	}

	@Test
	void deveRetornarStatus200_QuandoConsultarRestaurantes() {
		RestAssured.given().header(HttpHeaders.AUTHORIZATION, bearerTokenLeitura()).accept(ContentType.JSON).when()
				.get().then().statusCode(HttpStatus.OK.value());
	}

	@Test
	void deveRetornarStatus201_QuandoCadastrarRestaurante() {
		RestAssured.given().header(HttpHeaders.AUTHORIZATION, bearerTokenEdicao()).body(jsonRestauranteCorreto)
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().post().then()
				.statusCode(HttpStatus.CREATED.value());
	}

	@Test
	void deveRetornarStatus400_QuandoCadastrarRestauranteSemTaxaFrete() {
		RestAssured.given().header(HttpHeaders.AUTHORIZATION, bearerTokenEdicao()).body(jsonRestauranteSemFrete)
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().post().then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
	}

	@Test
	void deveRetornarStatus400_QuandoCadastrarRestauranteSemCozinha() {
		RestAssured.given().header(HttpHeaders.AUTHORIZATION, bearerTokenEdicao()).body(jsonRestauranteSemCozinha)
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().post().then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
	}

	@Test
	void deveRetornarStatus400_QuandoCadastrarRestauranteComCozinhaInexistente() {
		RestAssured.given().header(HttpHeaders.AUTHORIZATION, bearerTokenEdicao())
				.body(jsonRestauranteComCozinhaInexistente).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().post().then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}

	@Test
	void deveRetornarRespostaEStatusCorretos_QuandoConsultarRestauranteExistente() {
		RestAssured.given().header(HttpHeaders.AUTHORIZATION, bearerTokenLeitura())
				.pathParam("restauranteId", burgerTopRestaurante.getId()).accept(ContentType.JSON).when()
				.get("/{restauranteId}").then().statusCode(HttpStatus.OK.value())
				.body("nome", equalTo(burgerTopRestaurante.getNome()));
	}

	@Test
	void deveRetornarStatus404_QuandoConsultarRestauranteInexistente() {
		RestAssured.given().header(HttpHeaders.AUTHORIZATION, bearerTokenLeitura())
				.pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE).accept(ContentType.JSON).when()
				.get("/{restauranteId}").then().statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	void deveRetornarStatus401_QuandoConsultarRestaurantesSemToken() {
		RestAssured.given().accept(ContentType.JSON).when().get().then().statusCode(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	void deveRetornarStatus403_QuandoCadastrarRestauranteSemPermissao() {
		String token = OAuth2TestUtils.gerarToken(jwtEncoder, List.of("WRITE"));
		RestAssured.given().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).body(jsonRestauranteCorreto)
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().post().then()
				.statusCode(HttpStatus.FORBIDDEN.value());
	}

	private String bearerTokenLeitura() {
		return "Bearer " + OAuth2TestUtils.gerarToken(jwtEncoder, List.of("READ"));
	}

	private String bearerTokenEdicao() {
		return "Bearer " + OAuth2TestUtils.gerarToken(jwtEncoder, List.of("WRITE"), "EDITAR_RESTAURANTES");
	}

	private void prepararDados() {
		var estadoSaoPaulo = new Estado();
		estadoSaoPaulo.setNome("São Paulo");
		estadoSaoPaulo = estadoRepository.save(estadoSaoPaulo);
		cidadeSaoPaulo = new Cidade();
		cidadeSaoPaulo.setNome("São Paulo");
		cidadeSaoPaulo.setEstado(estadoSaoPaulo);
		cidadeSaoPaulo = cidadeRepository.save(cidadeSaoPaulo);
		var cozinhaBrasileira = new Cozinha();
		cozinhaBrasileira.setNome("Brasileira");
		cozinhaBrasileira = cozinhaRepository.save(cozinhaBrasileira);
		var cozinhaAmericana = new Cozinha();
		cozinhaAmericana.setNome("Americana");
		cozinhaAmericana = cozinhaRepository.save(cozinhaAmericana);
		burgerTopRestaurante = new Restaurante();
		burgerTopRestaurante.setNome("Burger Top");
		burgerTopRestaurante.setTaxaFrete(new BigDecimal("10"));
		burgerTopRestaurante.setCozinha(cozinhaAmericana);
		restauranteRepository.save(burgerTopRestaurante);
		var comidaMineiraRestaurante = new Restaurante();
		comidaMineiraRestaurante.setNome("Comida Mineira");
		comidaMineiraRestaurante.setTaxaFrete(new BigDecimal("10"));
		comidaMineiraRestaurante.setCozinha(cozinhaBrasileira);
		restauranteRepository.save(comidaMineiraRestaurante);
	}
}
