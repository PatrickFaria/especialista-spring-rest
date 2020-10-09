package com.patrick.algafoodapi.api.assembler;

import com.patrick.algafoodapi.api.model.input.UsuarioInput;
import com.patrick.algafoodapi.api.model.input.UsuarioInputSemSenha;
import com.patrick.algafoodapi.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Usuario toDomainObject(UsuarioInput usuarioInput){
        return modelMapper.map(usuarioInput, Usuario.class);
    }

    public void copyToDomainObject(UsuarioInput usuarioInput, Usuario usuario) {
        modelMapper.map(usuarioInput, usuario);
    }

    public void copyToDomainSemSenhaObject(UsuarioInputSemSenha usuarioInputSemSenha, Usuario usuario) {
        modelMapper.map(usuarioInputSemSenha, usuario);
    }

}
