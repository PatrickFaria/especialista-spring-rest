package com.patrick.algafoodapi.api.assembler;

import com.patrick.algafoodapi.api.model.input.FormaPagamentoInput;
import com.patrick.algafoodapi.api.model.input.RestauranteInput;
import com.patrick.algafoodapi.domain.model.Cozinha;
import com.patrick.algafoodapi.domain.model.FormaPagamento;
import com.patrick.algafoodapi.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormaPagamentoInputDisassembly {

    @Autowired
    private ModelMapper modelMapper;

    public FormaPagamento toDomainObject(FormaPagamentoInput formaPagamentoInput){
        return modelMapper.map(formaPagamentoInput, FormaPagamento.class);
    }

    public void copyToDomainObject(FormaPagamentoInput formaPagamentoInput, FormaPagamento formaPagamento){

        modelMapper.map(formaPagamentoInput, formaPagamento);
    }

}
