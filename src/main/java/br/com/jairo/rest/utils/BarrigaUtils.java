package br.com.jairo.rest.utils;

import io.restassured.RestAssured;

public class BarrigaUtils {

    public static Integer getIdContaPeloNome(String nome){
        return RestAssured.get("/contas?nome=" + nome).path("id[0]");
    }

    public static Integer getIdMovPelaDescricao(String desc){
        return RestAssured.get("/transacoes?descricao=" + desc).path("id[0]");
    }

}
