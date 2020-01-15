package com.patrick.algafoodapi.infrastructure.repository;

import com.patrick.algafoodapi.domain.model.Cidade;
import com.patrick.algafoodapi.domain.repository.CidadeRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CidadeRepositoryImpl implements CidadeRepository {


    @PersistenceContext
    private EntityManager manager;

    public List<Cidade> listar(){
        return manager.createQuery("from Cidade", Cidade.class).getResultList();
    }

    @Transactional
    public Cidade adicionar(Cidade cidade){
        return manager.merge(cidade);
    }

    public Cidade buscar(Long id){
        return manager.find(Cidade.class, id);
    }

    @Transactional
    public void remover(Long cidadeId){
        Cidade cidade = buscar(cidadeId);
        manager.remove(cidade);
    }
}
