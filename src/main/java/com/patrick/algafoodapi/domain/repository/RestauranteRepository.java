package com.patrick.algafoodapi.domain.repository;

import com.patrick.algafoodapi.domain.model.Restaurante;

import java.util.List;

public interface RestauranteRepository {

    List<Restaurante> listar();
    Restaurante buscar(Long id);
    Restaurante adicionar(Restaurante restaurante);
    void remover(Long restauranteId);

}
