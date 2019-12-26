package com.patrick.algafoodapi.domain.repository;

import com.patrick.algafoodapi.domain.model.FormaPagamento;

import java.util.List;

public interface FormaPagamentoRepository {

    List<FormaPagamento> todas();
    FormaPagamento porId(Long id);
    FormaPagamento adicionar(FormaPagamento formaPagamento);
    void remover(FormaPagamento formaPagamento);
}
