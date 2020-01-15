package com.patrick.algafoodapi.infrastructure.repository;

import com.patrick.algafoodapi.domain.model.Cozinha;
import com.patrick.algafoodapi.domain.repository.CozinhaRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CozinhaRepositoryImpl implements CozinhaRepository {

    @PersistenceContext
    private EntityManager manager;

    public List<Cozinha> listar(){
        return manager.createQuery("from Cozinha", Cozinha.class).getResultList();
    }

    @Transactional
    public Cozinha adicionar(Cozinha cozinha){
        return manager.merge(cozinha);
    }

    public Cozinha buscar(Long id){
        return manager.find(Cozinha.class, id);
    }

    @Transactional
    public void remover(Long id){
        Cozinha cozinha = buscar(id);

        if(cozinha == null){
            throw  new EmptyResultDataAccessException(1);
        }

        manager.remove(cozinha);
    }
}
