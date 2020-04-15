package com.algaworks.algafoodapi.domain.exception;

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;
    public static final String MSG_COZINHA_NAO_ENCONTRADA = "Não existe um cadasrto de cozinha com código %d";

    public CozinhaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public CozinhaNaoEncontradaException(Long cozinhaId) {
        this(String.format(MSG_COZINHA_NAO_ENCONTRADA, cozinhaId));
    }

}
