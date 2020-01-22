package com.patrick.algafoodapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrick.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.patrick.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.patrick.algafoodapi.domain.model.Cidade;
import com.patrick.algafoodapi.domain.model.FormaPagamento;
import com.patrick.algafoodapi.domain.model.Permissao;
import com.patrick.algafoodapi.domain.model.Restaurante;
import com.patrick.algafoodapi.domain.repository.PermissaoRepository;
import com.patrick.algafoodapi.domain.service.CadastroPermissaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/permissao")
public class PermissaoController {

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private CadastroPermissaoService cadastroPermissaoService;

    @GetMapping
    public List<Permissao> listar() {
        return permissaoRepository.findAll();
    }

    @GetMapping("/{permissaoId}")
    public ResponseEntity<Permissao> buscar(@PathVariable Long permissaoId) {
        Optional<Permissao> permissao = permissaoRepository.findById(permissaoId);
        if(permissao.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(permissao.get());
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody  Permissao permissao){
        try {
            permissao = cadastroPermissaoService.salvar(permissao);
            return ResponseEntity.status(HttpStatus.CREATED).body(permissao);
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{permissaoId}")
    public ResponseEntity<?> atualizar(@PathVariable Long permissaoId, @RequestBody Permissao permissao){
        try{
            Optional<Permissao> FormaPagamentoAtual= permissaoRepository.findById(permissaoId);
            if(FormaPagamentoAtual.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            BeanUtils.copyProperties(permissao,FormaPagamentoAtual.get(),"id");
            Permissao permissaoSalvo = cadastroPermissaoService.salvar(FormaPagamentoAtual.get());
            return ResponseEntity.ok().body(permissaoSalvo);
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PatchMapping("/{permissaoId}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long permissaoId, @RequestBody Map<String, Object> campos){
        Optional<Permissao> permissaoAtual = permissaoRepository.findById(permissaoId);

        if(permissaoAtual.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        merge(campos, permissaoAtual.get());

        return atualizar(permissaoId, permissaoAtual.get());
    }

    private void merge (Map<String, Object> dadosOrigem, Permissao permissaoDestino){
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

        dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
            Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
            field.setAccessible(true);
            Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
            ReflectionUtils.setField(field, permissaoDestino, novoValor);
        });
    }

    @DeleteMapping("/{permissaoId}")
    public ResponseEntity<Cidade> remover(@PathVariable Long permissaoId){
        try {
            cadastroPermissaoService.excluir(permissaoId);
            return ResponseEntity.noContent().build();
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();
        }catch (EntidadeEmUsoException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
