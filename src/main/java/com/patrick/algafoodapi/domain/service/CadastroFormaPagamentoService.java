package com.patrick.algafoodapi.domain.service;

import com.patrick.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.patrick.algafoodapi.domain.exception.PermissaoNaoEncontradaException;
import com.patrick.algafoodapi.domain.model.FormaPagamento;
import com.patrick.algafoodapi.domain.repository.FormaPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroFormaPagamentoService {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    public FormaPagamento salvar(FormaPagamento formaPagamento){
        return formaPagamentoRepository.save(formaPagamento);
    }

    @Transactional
    public void excluir(Long formaPamentoId){
        try {
            formaPagamentoRepository.deleteById(formaPamentoId);
            formaPagamentoRepository.flush();
        }catch (EmptyResultDataAccessException e){
            throw new PermissaoNaoEncontradaException(
                    String.format("Não existe um cadastro de forma de pagamento de código %d!", formaPamentoId)
            );
        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(
                    String.format("Forma de Pagamento de código %d não pode ser removida, pois está em uso!", formaPamentoId)
            );
        }
    }



}
