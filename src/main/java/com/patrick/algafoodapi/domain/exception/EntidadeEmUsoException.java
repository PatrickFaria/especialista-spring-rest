package com.patrick.algafoodapi.domain.exception;

public class EntidadeEmUsoException extends  RuntimeException {

    public EntidadeEmUsoException(String mensagem){
        super(mensagem);
    }
}
