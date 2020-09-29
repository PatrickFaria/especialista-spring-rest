package com.patrick.algafoodapi.api.assembler;

import com.patrick.algafoodapi.api.model.CozinhaModel;
import com.patrick.algafoodapi.api.model.RestauranteModel;
import com.patrick.algafoodapi.domain.model.Cozinha;
import com.patrick.algafoodapi.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CozinhaModelAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public CozinhaModel toModel(Cozinha cozinha) {
        return modelMapper.map(cozinha, CozinhaModel.class);
    }

    public List<CozinhaModel> toCollectionModel(List<Cozinha> cozinhas){
        return cozinhas.stream().map(this::toModel)
                .collect(Collectors.toList());
    }

}
