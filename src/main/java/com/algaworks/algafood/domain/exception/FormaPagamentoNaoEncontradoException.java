package com.algaworks.algafood.domain.exception;


public class FormaPagamentoNaoEncontradoException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;


    public FormaPagamentoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public FormaPagamentoNaoEncontradoException(Long id) {
        super(String.format("Não existe uma forma de pagamento com código %d", id));
    }

}
