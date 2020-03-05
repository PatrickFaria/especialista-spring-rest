package com.patrick.algafoodapi.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntidadeEmUsoException extends  RuntimeException {

    public EntidadeEmUsoException(String mensagem){
        super(mensagem);
    }
}
