package com.patrick.algafoodapi.api.controller;

import com.patrick.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.patrick.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.patrick.algafoodapi.domain.model.Cozinha;
import com.patrick.algafoodapi.domain.model.Estado;
import com.patrick.algafoodapi.domain.repository.EstadoRepository;
import com.patrick.algafoodapi.domain.service.CadastroEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estados")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    @GetMapping
    public List<Estado> listar() {
        return estadoRepository.listar();
    }

    @GetMapping("/{estadoId}")
    public ResponseEntity<Estado> buscar(@PathVariable Long estadoId) {
        Estado estado = estadoRepository.buscar(estadoId);
        if(estado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estado);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estado adicionar(@RequestBody Estado estado){
        return cadastroEstadoService.salvar(estado);
    }

    @PutMapping("/{estadoId}")
    public ResponseEntity<Estado> editar(@PathVariable Long estadoId, @RequestBody Estado estado){
        Estado estadoAtual = estadoRepository.buscar(estadoId);
        if(estadoAtual == null){
            return ResponseEntity.notFound().build();
        }
        BeanUtils.copyProperties(estado,estadoAtual,"id");
        estadoAtual = cadastroEstadoService.salvar(estadoAtual);
        return ResponseEntity.ok(estadoAtual);
    }

    @DeleteMapping("/{estadoId}")
    public ResponseEntity<Cozinha> remover(@PathVariable Long estadoId){
        try {
            cadastroEstadoService.excluir(estadoId);
            return ResponseEntity.noContent().build();
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();
        }catch (EntidadeEmUsoException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

}
