package com.patrick.algafoodapi.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UsuarioInputSemSenha {

    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

}
