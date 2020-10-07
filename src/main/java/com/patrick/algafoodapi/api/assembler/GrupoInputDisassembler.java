package com.patrick.algafoodapi.api.assembler;

import com.patrick.algafoodapi.api.model.input.CidadeInput;
import com.patrick.algafoodapi.api.model.input.GrupoInput;
import com.patrick.algafoodapi.domain.model.Cidade;
import com.patrick.algafoodapi.domain.model.Estado;
import com.patrick.algafoodapi.domain.model.Grupo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GrupoInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Grupo toDomainObject(GrupoInput grupoInput){
        return modelMapper.map(grupoInput, Grupo.class);
    }

    public void copyToDomainObject(GrupoInput grupoInput, Grupo grupo){

        modelMapper.map(grupoInput, grupo);
    }

}
