package com.patrick.algafoodapi.domain.repository;

import com.patrick.algafoodapi.domain.model.Cozinha;

import java.util.List;

public interface CozinhaRepository {

    List<Cozinha> listar();
    Cozinha buscar(Long id);
    Cozinha adicionar(Cozinha cozinha);
    void remover (Long id);

}
