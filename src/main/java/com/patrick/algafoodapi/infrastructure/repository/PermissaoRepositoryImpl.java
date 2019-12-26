package com.patrick.algafoodapi.infrastructure.repository;

import com.patrick.algafoodapi.domain.model.Permissao;
import com.patrick.algafoodapi.domain.repository.PermissaoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PermissaoRepositoryImpl implements PermissaoRepository {


    @PersistenceContext
    private EntityManager manager;

    public List<Permissao> todas(){
        return manager.createQuery("from Permissao", Permissao.class).getResultList();
    }

    @Transactional
    public Permissao adicionar(Permissao permissao){
        return manager.merge(permissao);
    }

    public Permissao porId(Long id){
        return manager.find(Permissao.class, id);
    }

    @Transactional
    public void remover(Permissao permissao){
        permissao = porId(permissao.getId());
        manager.remove(permissao);
    }
}
