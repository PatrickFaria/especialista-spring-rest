package com.patrick.algafoodapi.domain.service;

import com.patrick.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.patrick.algafoodapi.domain.exception.PermissaoNaoEncontradaException;
import com.patrick.algafoodapi.domain.model.Permissao;
import com.patrick.algafoodapi.domain.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroPermissaoService {

    @Autowired
    private PermissaoRepository permissaoRepository;

    public Permissao salvar(Permissao permissao){
        return permissaoRepository.save(permissao);
    }

    @Transactional
    public void excluir(Long permissaoId){
        try {
            permissaoRepository.deleteById(permissaoId);
            permissaoRepository.flush();
        }catch (EmptyResultDataAccessException e){
            throw new PermissaoNaoEncontradaException(
                    String.format("Não existe um cadastro de permissao de código %d!", permissaoId)
            );
        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(
                    String.format("Permissao de código %d não pode ser removida, pois está em uso!", permissaoId)
            );
        }
    }



}
