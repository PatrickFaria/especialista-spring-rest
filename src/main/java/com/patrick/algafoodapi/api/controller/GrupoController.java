package com.patrick.algafoodapi.api.controller;

import com.patrick.algafoodapi.api.assembler.CidadeInputDisassembler;
import com.patrick.algafoodapi.api.assembler.CidadeModelAssembler;
import com.patrick.algafoodapi.api.assembler.GrupoInputDisassembler;
import com.patrick.algafoodapi.api.assembler.GrupoModelAssembler;
import com.patrick.algafoodapi.api.model.CidadeModel;
import com.patrick.algafoodapi.api.model.GrupoModel;
import com.patrick.algafoodapi.api.model.input.CidadeInput;
import com.patrick.algafoodapi.api.model.input.GrupoInput;
import com.patrick.algafoodapi.domain.exception.EstadoNaoEncontradoException;
import com.patrick.algafoodapi.domain.exception.NegocioException;
import com.patrick.algafoodapi.domain.model.Cidade;
import com.patrick.algafoodapi.domain.model.Grupo;
import com.patrick.algafoodapi.domain.repository.CidadeRepository;
import com.patrick.algafoodapi.domain.repository.GrupoRepository;
import com.patrick.algafoodapi.domain.service.CadastroCidadeService;
import com.patrick.algafoodapi.domain.service.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/grupos")
public class GrupoController {

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private CadastroGrupoService cadastroGrupoService;

    @Autowired
    private GrupoModelAssembler grupoModelAssembler;

    @Autowired
    private GrupoInputDisassembler grupoInputDisassembler;

    @GetMapping
    public List<GrupoModel> listar() {
        List<Grupo> todasCidades = grupoRepository.findAll();

        return grupoModelAssembler.toCollectionModel(todasCidades);
    }

    @GetMapping("/{grupoId}")
    public GrupoModel buscar(@PathVariable Long grupoId) {
        Grupo grupo = cadastroGrupoService.buscarOuFalhar(grupoId);

        return grupoModelAssembler.toModel(grupo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput) {
        try {
            Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);

            grupo = cadastroGrupoService.salvar(grupo);

            return grupoModelAssembler.toModel(grupo);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @PutMapping("/{grupoId}")
    public GrupoModel atualizar(@PathVariable Long grupoId,
                                 @RequestBody @Valid GrupoInput grupoInput) {
        try {
            Grupo grupoAtual = cadastroGrupoService.buscarOuFalhar(grupoId);

            grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);

            grupoAtual = cadastroGrupoService.salvar(grupoAtual);

            return grupoModelAssembler.toModel(grupoAtual);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{grupoId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long grupoId) {
        cadastroGrupoService.excluir(grupoId);
    }


}
