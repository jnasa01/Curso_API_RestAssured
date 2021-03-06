package br.com.jairo.rest.refact;

import br.com.jairo.rest.core.BaseTest;
import br.com.jairo.rest.tests.Movimentacao;
import br.com.jairo.rest.utils.BarrigaUtils;
import br.com.jairo.rest.utils.DataUtils;
import org.junit.Test;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class MovimentacaoTest extends BaseTest {


    @Test
    public void deveInserirMovimentacaoSucesso() {

        Movimentacao mov = getMovimentacaoValida();

       given()
                .body(mov)
                .when()
                    .post("/transacoes")
                .then()
                    .statusCode(201)
                    .body("id", is(notNullValue()))
                   .body("descricao", is("Descricao Nova"))
                   .body("envolvido", is("Junto e Misturado"))
                   .body("observacao", is(nullValue()))
                    .body("tipo", is("REC"))
                   .body("data_pagamento", is(notNullValue()))
                   .body("valor", is(notNullValue()))
                   .body("status", is(true))
                   .body("conta_id", is(notNullValue()))
                   .body("usuario_id", is(13766))
                   .body("transferencia_id", is(nullValue()))
                   .body("parcelamento_id", is(nullValue()))
        ;

    }

    private Movimentacao getMovimentacaoValida(){
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(BarrigaUtils.getIdContaPeloNome("Conta para movimentacoes"));
        //mov.getUsuario_id(usuario_id);
        mov.setDescricao("Descricao Nova");
        mov.setEnvolvido("Junto e Misturado");
        mov.setTipo("REC");
        mov.setData_transacao(DataUtils.getDataDiferencaDias(-1));
        mov.setData_pagamento(DataUtils.getDataDiferencaDias(3));
        mov.setValor(153f);
        mov.setStatus(true);
        return mov;
    }

    @Test
    public void deveValidarCamposObrigatoriosMovimentacao() {

        given()
                .body("{}")
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", hasSize(8))
                .body("msg", hasItems(
                        "Data da Movimenta????o ?? obrigat??rio",
                        "Data do pagamento ?? obrigat??rio",
                        "Descri????o ?? obrigat??rio",
                        "Interessado ?? obrigat??rio",
                        "Valor ?? obrigat??rio",
                        "Valor deve ser um n??mero",
                        "Conta ?? obrigat??rio",
                        "Situa????o ?? obrigat??rio"
                ))
        ;

    }


    @Test
    public void naoDeveInserirMovimentacaoDataFutura() {

        Movimentacao mov = getMovimentacaoValida();
        mov.setData_transacao(DataUtils.getDataDiferencaDias(5));

        given()
                .body(mov)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", hasSize(1))
                .body("msg", hasItem("Data da Movimenta????o deve ser menor ou igual ?? data atual"))
        ;

    }

    @Test
    public void naoDeveRemoverContaComMovimentacao() {

        Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta com movimentacao");

        given()
                .pathParam("id", CONTA_ID)
                .when()
                .delete("/contas/{id}")
                .then()
                .statusCode(500)
                .body("constraint", is("transacoes_conta_id_foreign"))

        ;

    }

    @Test
    public void deveRemoverMovimentacao() {

        Integer MOV_ID = BarrigaUtils.getIdMovPelaDescricao("Movimentacao para exclusao");

        given()
                .pathParam("id", MOV_ID)
                .when()
                .delete("/transacoes/{id}")
                .then()
                .statusCode(204)
        ;

    }

}
