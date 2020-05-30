package com.algaworks.algafood;

import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class CadastroCozinhaIT {
	private static final int COZINHA_ID_INEXISTENTE = 100;
	@LocalServerPort
	private int port;
	@Autowired
	private DatabaseCleaner databaseCleaner;
	@Autowired
	private CozinhaRepository cozinhaRepository;
	private Cozinha cozinhaAmericana;
	private int quantidadeCozinhasCadastradas;
	private String jsonCorretoCozinhaChinesa;

	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = this.port;
		RestAssured.basePath = "/cozinhas";
		this.jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource("/json/correto/cozinha-chinesa.json");
		this.databaseCleaner.clearTables();
		this.prepararDados();
	}

	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas() {
		RestAssured.given().accept(ContentType.JSON).when().get().then().statusCode(200);
	}

	@Test
	public void deveRetornarQuantidadeCorretaDeCozinhas_QuandoConsultarCozinhas() {
		RestAssured.given().accept(ContentType.JSON).when().get().then().body("",
				Matchers.hasSize(this.quantidadeCozinhasCadastradas));
	}

	@Test
	public void deveRetornarStatus201_QuandoCadastrarCozinha() {
		RestAssured.given().body(this.jsonCorretoCozinhaChinesa).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().post().then().statusCode(201);
	}

	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
		RestAssured.given().pathParam("cozinhaId", this.cozinhaAmericana.getId()).accept(ContentType.JSON).when()
				.get("/{cozinhaId}").then().statusCode(200).body("nome", equalTo(this.cozinhaAmericana.getNome()));
	}

	@Test
	public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {
		RestAssured.given().pathParam("cozinhaId", COZINHA_ID_INEXISTENTE).accept(ContentType.JSON).when()
				.get("/{cozinhaId}").then().statusCode(404);
	}

	private void prepararDados() {
		Cozinha cozinhaTailandesa = new Cozinha();
		cozinhaTailandesa.setNome("Tailandesa");
		this.cozinhaRepository.save(cozinhaTailandesa);
		this.cozinhaAmericana = new Cozinha();
		this.cozinhaAmericana.setNome("Americana");
		this.cozinhaRepository.save(this.cozinhaAmericana);
		this.quantidadeCozinhasCadastradas = (int) this.cozinhaRepository.count();
	}
}
