package com.algaworks.algafood;

import static org.hamcrest.CoreMatchers.equalTo;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class CadastroRestauranteIT {
	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio",
			DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
	private static final int RESTAURANTE_ID_INEXISTENTE = 100;
	@LocalServerPort
	private int port;
	@Autowired
	private DatabaseCleaner databaseCleaner;
	@Autowired
	private CozinhaRepository cozinhaRepository;
	@Autowired
	private RestauranteRepository restauranteRepository;
	private String jsonRestauranteCorreto, jsonRestauranteSemFrete, jsonRestauranteSemCozinha,
			jsonRestauranteComCozinhaInexistente;
	private Restaurante burgerTopRestaurante;

	@BeforeEach
	public void setup() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = this.port;
		RestAssured.basePath = "/restaurantes";
		this.jsonRestauranteCorreto = ResourceUtils
				.getContentFromResource("/json/correto/restaurante-new-york-barbecue.json");
		this.jsonRestauranteSemFrete = ResourceUtils
				.getContentFromResource("/json/incorreto/restaurante-new-york-barbecue-sem-frete.json");
		this.jsonRestauranteSemCozinha = ResourceUtils
				.getContentFromResource("/json/incorreto/restaurante-new-york-barbecue-sem-cozinha.json");
		this.jsonRestauranteComCozinhaInexistente = ResourceUtils
				.getContentFromResource("/json/incorreto/restaurante-new-york-barbecue-com-cozinha-inexistente.json");
		this.databaseCleaner.clearTables();
		this.prepararDados();
	}

	@Test
	public void deveRetornarStatus200_QuandoConsultarRestaurantes() {
		RestAssured.given().accept(ContentType.JSON).when().get().then().statusCode(200);
	}

	@Test
	public void deveRetornarStatus201_QuandoCadastrarRestaurante() {
		RestAssured.given().body(this.jsonRestauranteCorreto).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().post().then().statusCode(201);
	}

	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteSemTaxaFrete() {
		RestAssured.given().body(this.jsonRestauranteSemFrete).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().post().then().statusCode(400).body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
	}

	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteSemCozinha() {
		RestAssured.given().body(this.jsonRestauranteSemCozinha).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().post().then().statusCode(400).body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
	}

	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteComCozinhaInexistente() {
		RestAssured.given().body(this.jsonRestauranteComCozinhaInexistente).contentType(ContentType.JSON)
				.accept(ContentType.JSON).when().post().then().statusCode(400)
				.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}

	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRestauranteExistente() {
		RestAssured.given().pathParam("restauranteId", this.burgerTopRestaurante.getId()).accept(ContentType.JSON)
				.when().get("/{restauranteId}").then().statusCode(200)
				.body("nome", equalTo(this.burgerTopRestaurante.getNome()));
	}

	@Test
	public void deveRetornarStatus404_QuandoConsultarRestauranteInexistente() {
		RestAssured.given().pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE).accept(ContentType.JSON).when()
				.get("/{restauranteId}").then().statusCode(404);
	}

	private void prepararDados() {
		Cozinha cozinhaBrasileira = new Cozinha();
		cozinhaBrasileira.setNome("Brasileira");
		this.cozinhaRepository.save(cozinhaBrasileira);
		Cozinha cozinhaAmericana = new Cozinha();
		cozinhaAmericana.setNome("Americana");
		this.cozinhaRepository.save(cozinhaAmericana);
		this.burgerTopRestaurante = new Restaurante();
		this.burgerTopRestaurante.setNome("Burger Top");
		this.burgerTopRestaurante.setTaxaFrete(new BigDecimal(10));
		this.burgerTopRestaurante.setCozinha(cozinhaAmericana);
		this.restauranteRepository.save(this.burgerTopRestaurante);
		Restaurante comidaMineiraRestaurante = new Restaurante();
		comidaMineiraRestaurante.setNome("Comida Mineira");
		comidaMineiraRestaurante.setTaxaFrete(new BigDecimal(10));
		comidaMineiraRestaurante.setCozinha(cozinhaBrasileira);
		this.restauranteRepository.save(comidaMineiraRestaurante);
	}
}
