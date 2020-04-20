package com.algaworks.algafoodapi.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
    PARAMETRO_INVALIDO("/paramentro-invalido", "Parâmetro inválido"),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
    ERRO_SISTEMA("/erro-sistema", "Erro no sistema"),
    DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
    ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio");

    private String title;
    private String uri;

    ProblemType(String path, String title) {
        this.uri = "https://algafood.com.br" + path;
        this.title = title;
    }
}
