package com.patrick.algafoodapi.api.controller;

import com.patrick.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.patrick.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
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

@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {
    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @GetMapping
    public List<Cidade> listar() {
        return cidadeRepository.listar();
    }

    @GetMapping("/{cidadeId}")
    public ResponseEntity<Cidade> buscar(@PathVariable Long cidadeId) {
        Cidade cidade = cidadeRepository.buscar(cidadeId);
        if(cidade == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cidade);
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Cidade cidade){
        try{
            cidade = cadastroCidadeService.salvar(cidade);
            return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{cidadeId}")
    public ResponseEntity<?> editar(@PathVariable Long cidadeId, @RequestBody Cidade cidade){
        try{
            Cidade cidadeAtual = cidadeRepository.buscar(cidadeId);
            if(cidadeAtual == null){
                return ResponseEntity.notFound().build();
            }
            BeanUtils.copyProperties(cidade,cidadeAtual,"id");
            cidadeAtual = cadastroCidadeService.salvar(cidadeAtual);
            return ResponseEntity.ok(cidadeAtual);
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{cidadeId}")
    public ResponseEntity<Cidade> remover(@PathVariable Long cidadeId){
        try {
            cadastroCidadeService.excluir(cidadeId);
            return ResponseEntity.noContent().build();
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();
        }catch (EntidadeEmUsoException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
