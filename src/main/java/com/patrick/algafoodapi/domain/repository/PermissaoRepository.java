package com.patrick.algafoodapi.domain.repository;

import com.patrick.algafoodapi.domain.model.Permissao;

import java.util.List;

public interface PermissaoRepository {

    List<Permissao> todas();
    Permissao porId(Long id);
    Permissao adicionar(Permissao permissao);
    void remover(Permissao permissao);

}
