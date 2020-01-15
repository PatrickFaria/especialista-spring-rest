package com.patrick.algafoodapi.domain.repository;

import com.patrick.algafoodapi.domain.model.Cidade;

import java.util.List;

public interface CidadeRepository {

    List<Cidade> listar();
    Cidade buscar(Long id);
    Cidade adicionar(Cidade cidade);
    void remover(Long cidadeId);

}
