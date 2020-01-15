package com.patrick.algafoodapi.api.controller;

import com.patrick.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.patrick.algafoodapi.domain.model.Restaurante;
import com.patrick.algafoodapi.domain.repository.RestauranteRepository;
import com.patrick.algafoodapi.domain.service.CadastroRestauranteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @GetMapping
    public List<Restaurante> listar() {
        return restauranteRepository.listar();
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {
        Restaurante restaurante = restauranteRepository.buscar(restauranteId);
        if(restaurante == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(restaurante);
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody  Restaurante restaurante){
        try {
            restaurante = cadastroRestauranteService.salvar(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/{restauranteId}")
    public ResponseEntity<?> editar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante){
        try{
            Restaurante restauranteAtual= restauranteRepository.buscar(restauranteId);
            if(restauranteAtual == null){
                return ResponseEntity.notFound().build();
            }
            BeanUtils.copyProperties(restaurante,restauranteAtual,"id");
            restauranteAtual = cadastroRestauranteService.salvar(restauranteAtual);
            return ResponseEntity.ok().body(restauranteAtual);
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
