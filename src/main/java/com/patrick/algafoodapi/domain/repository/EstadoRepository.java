package com.patrick.algafoodapi.domain.repository;

import com.patrick.algafoodapi.domain.model.Estado;

import java.util.List;

public interface EstadoRepository {

    List<Estado> listar();
    Estado buscar(Long id);
    Estado adicionar(Estado estado);
    void remover(Long estadoId);

}
