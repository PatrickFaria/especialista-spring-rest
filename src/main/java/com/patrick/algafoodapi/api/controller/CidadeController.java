package com.patrick.algafoodapi.api.controller;

import com.patrick.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.patrick.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.patrick.algafoodapi.domain.exception.NegocioException;
import com.patrick.algafoodapi.domain.model.Cidade;
import com.patrick.algafoodapi.domain.repository.CidadeRepository;
import com.patrick.algafoodapi.domain.service.CadastroCidadeService;
import org.apache.coyote.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {
    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @GetMapping
    public List<Cidade> listar() {
        return cidadeRepository.findAll();
    }

    @GetMapping("/{cidadeId}")
    public Cidade buscar(@PathVariable Long cidadeId) {
        return cadastroCidadeService.buscarOuFalhar(cidadeId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Cidade adicionar(@RequestBody Cidade cidade) {
        try{
            return cadastroCidadeService.salvar(cidade);
        }catch (EntidadeNaoEncontradaException e){
            throw new NegocioException(e.getMessage());
        }
    }

    @PutMapping("/{cidadeId}")
    public Cidade editar(@PathVariable Long cidadeId, @RequestBody Cidade cidade) {
        Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(cidadeId);

        BeanUtils.copyProperties(cidade, cidadeAtual, "id");

        try{
            return cadastroCidadeService.salvar(cidadeAtual);
        }catch (EntidadeNaoEncontradaException e){
            throw new NegocioException(e.getMessage());
        }
    }

    @DeleteMapping("/{cidadeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cidadeId) {
        cadastroCidadeService.excluir(cidadeId);
    }
}
