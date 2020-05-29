package com.patrick.algafoodapi;

import com.patrick.algafoodapi.domain.model.Cozinha;
import com.patrick.algafoodapi.domain.model.Restaurante;
import com.patrick.algafoodapi.domain.repository.CozinhaRepository;
import com.patrick.algafoodapi.domain.repository.RestauranteRepository;
import com.patrick.algafoodapi.util.DatabaseCleaner;
import com.patrick.algafoodapi.util.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.flywaydb.core.Flyway;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroRestauranteIT {

    @LocalServerPort
    private int port;

    @Autowired
    private Flyway flyway;

    @Autowired(required = true)
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio!";

    private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos.";

    private static final int RESTAURANTE_ID_INEXISTENTE = 100;

    private Restaurante restauranteAmericano;
    private int quantidadeRestaurantesCadastrados;
    private String jsonCorretoRestauranteChines;
    private String jsonCorretoRestauranteChinesCozinhaInexistente;
    private String jsonCorretoRestauranteChinesSemCozinha;
    private String jsonCorretoRestauranteChinesSemTaxaFrete;

    @Before
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/restaurantes";
        jsonCorretoRestauranteChines = ResourceUtils.getContentFromResource(
                "/json/correto/restaurante-chines.json");
        jsonCorretoRestauranteChinesCozinhaInexistente = ResourceUtils.getContentFromResource(
                "/json/incorreto/restaurante-chines-cozinha-inexistente.json");
        jsonCorretoRestauranteChinesSemCozinha = ResourceUtils.getContentFromResource(
                "/json/incorreto/restaurante-chines-sem-cozinha.json");
        jsonCorretoRestauranteChinesSemTaxaFrete = ResourceUtils.getContentFromResource(
                "/json/incorreto/restaurante-chines-sem-taxa-frete.json");

        databaseCleaner.clearTables();
        prepararDados();
        //flyway.migrate();
    }

    @Test
    public void deveRetornarStatus200_QuandoConsultarRestaurante() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void deveRetornar400_QuandoCadastrarRestauranteSemTaxaFrete(){
        RestAssured.given()
                .body(jsonCorretoRestauranteChinesSemTaxaFrete)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", CoreMatchers.equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
    }

    @Test
    public void deveRetornar400_QuandoCadastrarRestauranteSemCozinha(){
        RestAssured.given()
                .body(jsonCorretoRestauranteChinesSemCozinha)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", CoreMatchers.equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
    }

    @Test
    public void deveRetornar400_QuandoCadastrarRestauranteCozinhaInexistente(){
        RestAssured.given()
                .body(jsonCorretoRestauranteChinesCozinhaInexistente)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", CoreMatchers.equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
    }

    @Test
    public void deveConterRestaurantes_QuandoConsultarRestaurantes() {

        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .body("", Matchers.hasSize(quantidadeRestaurantesCadastrados));
    }

    @Test
    public void deveRetornarStatus201_QuandoCadastrarRestaurante() {

        RestAssured.given()
                .body(jsonCorretoRestauranteChines)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRestauranteExistente() {
        RestAssured.given()
                .pathParam("restauranteId", restauranteAmericano.getId())
                .accept(ContentType.JSON)
                .when()
                .get("/{restauranteId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", CoreMatchers.equalTo(restauranteAmericano.getNome()));
    }

    @Test
    public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {
        RestAssured.given()
                .pathParam("cozinhaId", RESTAURANTE_ID_INEXISTENTE)
                .accept(ContentType.JSON)
                .when()
                .get("/{cozinhaId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private void prepararDados() {
        Cozinha cozinhaTailandesa = new Cozinha();
        cozinhaTailandesa.setNome("Tailandesa");
        cozinhaRepository.save(cozinhaTailandesa);

        Cozinha cozinhaAmericana = new Cozinha();
        cozinhaAmericana.setNome("Americana");
        cozinhaRepository.save(cozinhaAmericana);

        Restaurante restauranteTailandes = new Restaurante();
        restauranteTailandes.setNome("Tailandesa");
        restauranteTailandes.setTaxaFrete(new BigDecimal(15));
        restauranteTailandes.setCozinha(cozinhaTailandesa);
        restauranteRepository.save(restauranteTailandes);

        restauranteAmericano = new Restaurante();
        restauranteAmericano.setNome("Mc Donalds");
        restauranteAmericano.setTaxaFrete(new BigDecimal(10));
        restauranteAmericano.setCozinha(cozinhaAmericana);
        restauranteRepository.save(restauranteAmericano);

        quantidadeRestaurantesCadastrados = (int) restauranteRepository.count();
    }

//    @Autowired
//    private CadastroCozinhaService cadastroCozinhaService;
//
//    @Test
//    public void testarCadastroCozinhaComSucesso() {
//        //cenário
//        Cozinha novaCozinha = new Cozinha();
//        novaCozinha.setNome("Chinesa");
//        //ação
//        novaCozinha = cadastroCozinhaService.salvar(novaCozinha);
//        //validação
//        Assert.assertNotNull(novaCozinha);
//        Assert.assertNotNull(novaCozinha.getId());
//    }
//
//    @Test(expected = ConstraintViolationException.class)
//    public void testarCadastroCozinhaSemNome(){
//        Cozinha novaCozinha = new Cozinha();
//        novaCozinha.setNome(null);
//
//        novaCozinha = cadastroCozinhaService.salvar(novaCozinha);
//    }
//
//    @Test(expected = EntidadeEmUsoException.class)
//    public void deveFaharQuandoExcluirCozinhaEmUso(){
//        cadastroCozinhaService.excluir(1L);
//    }
//
//    @Test(expected = CozinhaNaoEncontradaException.class)
//    public void deveFaharQuandoExcluirCozinhaInexistente(){
//        cadastroCozinhaService.excluir(100L);
//    }

}

