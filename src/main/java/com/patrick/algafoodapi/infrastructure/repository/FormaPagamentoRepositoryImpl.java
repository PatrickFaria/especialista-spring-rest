package com.patrick.algafoodapi.infrastructure.repository;

import com.patrick.algafoodapi.domain.model.FormaPagamento;
import com.patrick.algafoodapi.domain.repository.FormaPagamentoRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

public class FormaPagamentoRepositoryImpl implements FormaPagamentoRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<FormaPagamento> todas(){
        return manager.createQuery("from formaPagamento", FormaPagamento.class).getResultList();
    }

    @Override
    public FormaPagamento porId(Long id){
        return manager.find(FormaPagamento.class, id);
    }

    @Override
    @Transactional
    public FormaPagamento adicionar(FormaPagamento formaPagamento) {
        return manager.merge(formaPagamento);
    }

    @Override
    @Transactional
    public void remover(FormaPagamento formaPagamento) {
        formaPagamento = porId(formaPagamento.getId());
        manager.remove(formaPagamento);
    }

}
