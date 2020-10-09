package com.patrick.algafoodapi.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UsuarioInput extends UsuarioInputSemSenha{

    @NotBlank
    private String senha;

}
