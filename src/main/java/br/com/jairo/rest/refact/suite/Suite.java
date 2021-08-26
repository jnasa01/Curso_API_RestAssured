package br.com.jairo.rest.refact.suite;

import br.com.jairo.rest.core.BaseTest;
import br.com.jairo.rest.refact.AuthTest;
import br.com.jairo.rest.refact.ContasTest;
import br.com.jairo.rest.refact.MovimentacaoTest;
import br.com.jairo.rest.refact.SaldoTest;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

@RunWith(org.junit.runners.Suite.class)
@org.junit.runners.Suite.SuiteClasses({
        ContasTest.class,
        MovimentacaoTest.class,
        SaldoTest.class,
        AuthTest.class
})

public class Suite extends BaseTest {
    @BeforeClass // executa antes de cada Classe
    public static void login() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "jnasa01@hotmail.com");
        login.put("senha", "Jnasa550301");

        String TOKEN = given()
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token");

        requestSpecification.header("Authorization", "JWT " + TOKEN);
        RestAssured.get("/reset").then().statusCode(200);
    }


}
