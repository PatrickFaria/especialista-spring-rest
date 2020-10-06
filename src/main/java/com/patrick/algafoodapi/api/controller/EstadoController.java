package com.patrick.algafoodapi.api.controller;

import com.patrick.algafoodapi.api.assembler.EstadoInputDisassembler;
import com.patrick.algafoodapi.api.assembler.EstadoModelAssembler;
import com.patrick.algafoodapi.api.model.EstadoModel;
import com.patrick.algafoodapi.api.model.input.EstadoInput;
import com.patrick.algafoodapi.domain.model.Estado;
import com.patrick.algafoodapi.domain.repository.EstadoRepository;
import com.patrick.algafoodapi.domain.service.CadastroEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/estados")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    @Autowired
    private EstadoModelAssembler estadoModelAssembler;

    @Autowired
    private EstadoInputDisassembler estadoInputDisassembler;

    @GetMapping
    public List<EstadoModel> listar() {
        List<Estado> todosEstados = estadoRepository.findAll();

        return estadoModelAssembler.toCollectionModel(todosEstados);
    }

    @GetMapping("/{estadoId}")
    public EstadoModel buscar(@PathVariable Long estadoId) {
        Estado estado = cadastroEstadoService.buscarOuFalhar(estadoId);

        return estadoModelAssembler.toModel(estado);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
        Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);

        estado = cadastroEstadoService.salvar(estado);

        return estadoModelAssembler.toModel(estado);
    }

    @PutMapping("/{estadoId}")
    public EstadoModel atualizar(@PathVariable Long estadoId,
                                 @RequestBody @Valid EstadoInput estadoInput) {
        Estado estadoAtual =  cadastroEstadoService.buscarOuFalhar(estadoId);

        estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);

        estadoAtual = cadastroEstadoService.salvar(estadoAtual);

        return estadoModelAssembler.toModel(estadoAtual);
    }

    @DeleteMapping("/{estadoId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long estadoId){

        cadastroEstadoService.excluir(estadoId);
    }

}
