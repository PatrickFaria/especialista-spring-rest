package com.patrick.algafoodapi.api.controller;

import com.patrick.algafoodapi.api.assembler.FormaPagamentoInputDisassembly;
import com.patrick.algafoodapi.api.assembler.FormaPagamentoModelAssembler;
import com.patrick.algafoodapi.api.model.FormaPagamentoModel;
import com.patrick.algafoodapi.api.model.input.FormaPagamentoInput;
import com.patrick.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.patrick.algafoodapi.domain.exception.NegocioException;
import com.patrick.algafoodapi.domain.model.FormaPagamento;
import com.patrick.algafoodapi.domain.repository.FormaPagamentoRepository;
import com.patrick.algafoodapi.domain.service.CadastroFormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/forma-pagamento")
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private CadastroFormaPagamentoService cadastroFormaPagamentoService;

    @Autowired
    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;

    @Autowired
    private FormaPagamentoInputDisassembly formaPagamentoInputDisassembly;

    @GetMapping
    public List<FormaPagamento> listar() {
        return formaPagamentoRepository.findAll();
    }

    @GetMapping("/{formaPamentoId}")
    public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long formaPamentoId) {
        Optional<FormaPagamento> formaPagamento = formaPagamentoRepository.findById(formaPamentoId);
        if(formaPagamento.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(formaPagamentoModelAssembler.toModel(formaPagamento.get()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoModel adicionar(@Valid @RequestBody FormaPagamentoInput formaPagamentoInput){
        try {
            FormaPagamento formaPagamento = formaPagamentoInputDisassembly.toDomainObject(formaPagamentoInput);
            return formaPagamentoModelAssembler.toModel(cadastroFormaPagamentoService.salvar(formaPagamento));
        }catch (EntidadeNaoEncontradaException e){
            throw new NegocioException(e.getMessage());
        }
    }

    @PutMapping("/{formaPagamentoId}")
    public FormaPagamentoModel atualizar(@PathVariable Long formaPagamentoId, @Valid @RequestBody FormaPagamentoInput formaPagamentoInput) {
        FormaPagamento formaPagamentoAtual = cadastroFormaPagamentoService.buscarOrFalhar(formaPagamentoId);

        formaPagamentoInputDisassembly.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);

        return formaPagamentoModelAssembler.toModel(cadastroFormaPagamentoService.salvar(formaPagamentoAtual));
    }

    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long formaPagamentoId){
            cadastroFormaPagamentoService.excluir(formaPagamentoId);
    }

}
