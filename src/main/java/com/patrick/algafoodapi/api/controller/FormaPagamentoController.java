package com.patrick.algafoodapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrick.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.patrick.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.patrick.algafoodapi.domain.model.Cidade;
import com.patrick.algafoodapi.domain.model.FormaPagamento;
import com.patrick.algafoodapi.domain.model.Restaurante;
import com.patrick.algafoodapi.domain.repository.FormaPagamentoRepository;
import com.patrick.algafoodapi.domain.service.CadastroFormaPagamentoService;
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
@RequestMapping(value = "/forma-pagamento")
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private CadastroFormaPagamentoService cadastroFormaPagamentoService;

    @GetMapping
    public List<FormaPagamento> listar() {
        return formaPagamentoRepository.findAll();
    }

    @GetMapping("/{formaPamentoId}")
    public ResponseEntity<FormaPagamento> buscar(@PathVariable Long formaPamentoId) {
        Optional<FormaPagamento> formaPagamento = formaPagamentoRepository.findById(formaPamentoId);
        if(formaPagamento.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(formaPagamento.get());
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody  FormaPagamento formaPagamento){
        try {
            formaPagamento = cadastroFormaPagamentoService.salvar(formaPagamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(formaPagamento);
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{formaPagamentoId}")
    public ResponseEntity<?> atualizar(@PathVariable Long formaPagamentoId, @RequestBody FormaPagamento formaPagamento){
        try{
            Optional<FormaPagamento> FormaPagamentoAtual= formaPagamentoRepository.findById(formaPagamentoId);
            if(FormaPagamentoAtual.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            BeanUtils.copyProperties(formaPagamento,FormaPagamentoAtual.get(),"id");
            FormaPagamento formaPagamentoSalvo = cadastroFormaPagamentoService.salvar(FormaPagamentoAtual.get());
            return ResponseEntity.ok().body(formaPagamentoSalvo);
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PatchMapping("/{formaPagamentoId}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long formaPagamentoId, @RequestBody Map<String, Object> campos){
        Optional<FormaPagamento> formaPagamentoAtual = formaPagamentoRepository.findById(formaPagamentoId);

        if(formaPagamentoAtual.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        merge(campos, formaPagamentoAtual.get());

        return atualizar(formaPagamentoId, formaPagamentoAtual.get());
    }

    private void merge (Map<String, Object> dadosOrigem, FormaPagamento formaPagamentoDestino){
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

        dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
            Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
            field.setAccessible(true);
            Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
            ReflectionUtils.setField(field, formaPagamentoDestino, novoValor);
        });
    }

    @DeleteMapping("/{formaPagamentoId}")
    public ResponseEntity<Cidade> remover(@PathVariable Long formaPagamentoId){
        try {
            cadastroFormaPagamentoService.excluir(formaPagamentoId);
            return ResponseEntity.noContent().build();
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();
        }catch (EntidadeEmUsoException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
