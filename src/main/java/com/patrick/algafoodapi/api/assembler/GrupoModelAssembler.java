package com.patrick.algafoodapi.api.assembler;

import com.patrick.algafoodapi.api.model.CidadeModel;
import com.patrick.algafoodapi.api.model.GrupoModel;
import com.patrick.algafoodapi.domain.model.Cidade;
import com.patrick.algafoodapi.domain.model.Grupo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GrupoModelAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public GrupoModel toModel(Grupo grupo) {
        return modelMapper.map(grupo, GrupoModel.class);
    }

    public List<GrupoModel> toCollectionModel(List<Grupo> grupos){
        return grupos.stream().map(this::toModel)
                .collect(Collectors.toList());
    }

}
