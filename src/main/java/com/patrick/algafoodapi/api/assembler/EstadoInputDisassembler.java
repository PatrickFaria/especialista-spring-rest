package com.patrick.algafoodapi.api.assembler;

import com.patrick.algafoodapi.api.model.input.CidadeInput;
import com.patrick.algafoodapi.api.model.input.EstadoInput;
import com.patrick.algafoodapi.domain.model.Cidade;
import com.patrick.algafoodapi.domain.model.Estado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EstadoInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Estado toDomainObject(EstadoInput estadoInput){
        return modelMapper.map(estadoInput, Estado.class);
    }

    public void copyToDomainObject(EstadoInput estadoInput, Estado estado){
        modelMapper.map(estadoInput, estado);
    }

}
