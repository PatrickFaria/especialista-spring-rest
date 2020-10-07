package com.patrick.algafoodapi.domain.service;

import com.patrick.algafoodapi.domain.exception.CidadeNaoEncontradaException;
import com.patrick.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.patrick.algafoodapi.domain.exception.GrupoNaoEncontradoException;
import com.patrick.algafoodapi.domain.model.Cidade;
import com.patrick.algafoodapi.domain.model.Estado;
import com.patrick.algafoodapi.domain.model.Grupo;
import com.patrick.algafoodapi.domain.repository.CidadeRepository;
import com.patrick.algafoodapi.domain.repository.EstadoRepository;
import com.patrick.algafoodapi.domain.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroGrupoService {

    public static final String GRUPO_EM_USO = "Grupo de código %d não pode ser removida, pois está em uso!";

    @Autowired
    private GrupoRepository grupoRepository;

    @Transactional
    public Grupo salvar(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    @Transactional
    public void excluir(Long grupoId) {
        try {
            grupoRepository.deleteById(grupoId);
            grupoRepository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new GrupoNaoEncontradoException(grupoId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(GRUPO_EM_USO, grupoId)
            );
        }
    }

    public Grupo buscarOuFalhar(Long grupoId) {
        return grupoRepository.findById(grupoId).orElseThrow(() ->
                new GrupoNaoEncontradoException(grupoId));
    }
}
