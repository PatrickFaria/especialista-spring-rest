package com.patrick.algafoodapi.domain.service;

import com.patrick.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.patrick.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.patrick.algafoodapi.domain.model.Cidade;
import com.patrick.algafoodapi.domain.model.Cozinha;
import com.patrick.algafoodapi.domain.model.Estado;
import com.patrick.algafoodapi.domain.repository.CidadeRepository;
import com.patrick.algafoodapi.domain.repository.CozinhaRepository;
import com.patrick.algafoodapi.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroCidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    public Cidade salvar(Cidade cidade){
        Long estadoId = cidade.getEstado().getId();
        Estado estado = estadoRepository.buscar(estadoId);
        if(estado == null){
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe cadastro de estado com código %d!", estadoId)
            );
        }
        cidade.setEstado(estado);
        return cidadeRepository.adicionar(cidade);
    }

    public void excluir(Long cidadeId){
        try {
            cidadeRepository.remover(cidadeId);
        }catch (EmptyResultDataAccessException e){
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe um cadastro de cidade de código %d !", cidadeId)
            );
        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(
                    String.format("Cidade de código %d não pode ser removida, pois está em uso!", cidadeId)
            );
        }
    }

}
