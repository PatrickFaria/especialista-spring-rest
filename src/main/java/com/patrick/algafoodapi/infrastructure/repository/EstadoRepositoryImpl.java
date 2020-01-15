package com.patrick.algafoodapi.infrastructure.repository;

import com.patrick.algafoodapi.domain.model.Estado;
import com.patrick.algafoodapi.domain.repository.EstadoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class EstadoRepositoryImpl implements EstadoRepository {


    @PersistenceContext
    private EntityManager manager;

    public List<Estado> listar(){
        return manager.createQuery("from Estado", Estado.class).getResultList();
    }

    @Transactional
    public Estado adicionar(Estado estado){
        return manager.merge(estado);
    }

    public Estado buscar(Long id){
        return manager.find(Estado.class, id);
    }

    @Transactional
    public void remover(Long estadoId){
        Estado estado = buscar(estadoId);
        manager.remove(estado);
    }
}
