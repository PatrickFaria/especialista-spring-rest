package com.patrick.algafoodapi.api.model;

import com.patrick.algafoodapi.domain.model.Cozinha;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class RestauranteModel {

    private  Long id;

    private String nome;

    private BigDecimal taxaFrete;

    private CozinhaModel cozinha;

    private Boolean ativo;

}
