package com.patrick.algafoodapi.api.controller;

import com.patrick.algafoodapi.api.controller.api.model.CozinhasXmlWrapper;
import com.patrick.algafoodapi.domain.model.Cozinha;
import com.patrick.algafoodapi.domain.repository.CozinhaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/cozinhas")
public class CozinhaController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @GetMapping
    public List<Cozinha> listar() {
        return cozinhaRepository.listar();
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public CozinhasXmlWrapper listarXML() {
        return new CozinhasXmlWrapper(cozinhaRepository.listar());
    }

    @GetMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
        Cozinha cozinha = cozinhaRepository.porId(cozinhaId);
        //return ResponseEntity.status(HttpStatus.OK).body(cozinha);
        if(cozinha == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cozinha);
        /*HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "http://localhost:8080/cozinhas");
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();*/
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cozinha adicionar(@RequestBody Cozinha cozinha){
        return cozinhaRepository.adicionar(cozinha);
    }

    @PutMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> editar(@PathVariable Long cozinhaId, @RequestBody Cozinha cozinha){
        Cozinha cozinhaAtual = cozinhaRepository.porId(cozinhaId);
        //cozinhaAtual.setNome(cozinha.getNome());
        if(cozinhaAtual == null){
            return ResponseEntity.notFound().build();
        }
        BeanUtils.copyProperties(cozinha,cozinhaAtual,"id");
        cozinhaRepository.adicionar(cozinhaAtual);
        return ResponseEntity.ok(cozinhaAtual);
    }

    @DeleteMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId){
        try {
            Cozinha cozinha = cozinhaRepository.porId(cozinhaId);
            if(cozinha == null){
                return ResponseEntity.notFound().build();
            }
            cozinhaRepository.remover(cozinha);
            return ResponseEntity.noContent().build();
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }
}
