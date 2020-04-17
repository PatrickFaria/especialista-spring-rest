package com.patrick.algafoodapi.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem Incompreensivel!"),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado!"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso!"),
    ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio!"),
    PARAMETRO_INVALIDO("/parametro_invalido", "Parâmetro inválido!");

    private String title;
    private String uri;

    ProblemType(String path, String title){
        this.uri  = "http://algafood.com.br"+path;
        this.title = title;
    }

}
