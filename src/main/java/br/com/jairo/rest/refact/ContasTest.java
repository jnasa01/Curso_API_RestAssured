package br.com.jairo.rest.refact;

import br.com.jairo.rest.core.BaseTest;
import br.com.jairo.rest.utils.BarrigaUtils;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class ContasTest extends BaseTest {

    @Test
    public void deveIncluirContaComSucesso() {
        given()
                .body("{\"nome\": \"Conta Inserida\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(201)

        ;

    }

    @Test
    public void deveAlterarContaComSucesso() {
        Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta para alterar");
        given()
                .body("{\"nome\": \" Conta Alterada\"}")
                .pathParam("id", CONTA_ID)
                .when()
                .put("/contas/{id}")
                .then()
                .statusCode(200)
                .body("id", is(CONTA_ID))
                .body("nome", is(" Conta Alterada"))

        ;
    }


    @Test
    public void naoDeveInserirContaComMesmoNome() {

        given()
                .body("{\"nome\": \"Conta mesmo nome\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }

}
