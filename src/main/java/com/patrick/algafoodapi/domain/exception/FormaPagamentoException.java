package com.patrick.algafoodapi.domain.exception;

public class FormaPagamentoException extends EntidadeNaoEncontradaException {

    public FormaPagamentoException(String mensagem) {
        super(mensagem);
    }

    public FormaPagamentoException(Long formaPagamentoId) {
        this(String.format("Não existe um cadastro de forma de pagamento de código %d!", formaPagamentoId));
    }
}
