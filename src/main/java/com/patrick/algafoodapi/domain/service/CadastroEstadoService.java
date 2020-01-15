package com.patrick.algafoodapi.domain.service;

import com.patrick.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.patrick.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.patrick.algafoodapi.domain.model.Estado;
import com.patrick.algafoodapi.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroEstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public Estado salvar(Estado estado){
        return estadoRepository.adicionar(estado);
    }

    public void excluir(Long estadoId){
        try {
            estadoRepository.remover(estadoId);
        }catch (EmptyResultDataAccessException e){
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe um cadastro de estado de código %d !", estadoId)
            );
        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(
                    String.format("Estado de código %d não pode ser removida, pois está em uso!", estadoId)
            );
        }
    }

}
