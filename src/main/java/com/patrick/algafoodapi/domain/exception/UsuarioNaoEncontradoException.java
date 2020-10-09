package com.patrick.algafoodapi.domain.exception;

public class UsuarioNaoEncontradoException extends EntidadeNaoEncontradaException{

    public UsuarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public UsuarioNaoEncontradoException(Long usuarioId){
        this(String.format("Não existe um cadastro de usuario de código %d!", usuarioId));
    }

}
