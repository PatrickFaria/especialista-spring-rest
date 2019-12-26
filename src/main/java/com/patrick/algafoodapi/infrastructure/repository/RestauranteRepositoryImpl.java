package com.patrick.algafoodapi.infrastructure.repository;

import com.patrick.algafoodapi.domain.model.Restaurante;
import com.patrick.algafoodapi.domain.repository.RestauranteRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepository {


    @PersistenceContext
    private EntityManager manager;

    public List<Restaurante> todas() {
        return manager.createQuery("from Restaurante", Restaurante.class).getResultList();
    }

    @Transactional
    public Restaurante adicionar(Restaurante restaurante) {
        return manager.merge(restaurante);
    }

    public Restaurante porId(Long id) {
        return manager.find(Restaurante.class, id);
    }

    @Transactional
    public void remover(Restaurante restaurante) {
        restaurante = porId(restaurante.getId());
        manager.remove(restaurante);
    }
}
