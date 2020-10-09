package com.patrick.algafoodapi.api.controller;

import com.patrick.algafoodapi.api.assembler.UsuarioInputDisassembler;
import com.patrick.algafoodapi.api.assembler.UsuarioModelAssembler;
import com.patrick.algafoodapi.api.model.UsuarioModel;
import com.patrick.algafoodapi.api.model.input.UsuarioInput;
import com.patrick.algafoodapi.api.model.input.UsuarioInputSemSenha;
import com.patrick.algafoodapi.api.model.input.SenhaInput;
import com.patrick.algafoodapi.domain.exception.EstadoNaoEncontradoException;
import com.patrick.algafoodapi.domain.exception.NegocioException;
import com.patrick.algafoodapi.domain.model.Usuario;
import com.patrick.algafoodapi.domain.repository.UsuarioRespository;
import com.patrick.algafoodapi.domain.service.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;

    @Autowired
    private UsuarioInputDisassembler usuarioInputDisassembler;

    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;

    @Autowired
    private UsuarioRespository usuarioRespository;

    @GetMapping
    public List<UsuarioModel> listar() {
        List<Usuario> todosUsuarios = usuarioRespository.findAll();

        return usuarioModelAssembler.toCollectionModel(todosUsuarios);
    }

    @GetMapping("/{usuarioId}")
    public UsuarioModel buscar(@PathVariable Long usuarioId) {
        Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);

        return usuarioModelAssembler.toModel(usuario);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel adicionar(@RequestBody @Valid UsuarioInput usuarioInput) {
        try {
            Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioInput);
            usuario.setDataCadastro(OffsetDateTime.now());

            usuario = cadastroUsuarioService.salvar(usuario);

            return usuarioModelAssembler.toModel(usuario);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @PutMapping("/{usuarioId}")
    public UsuarioModel atualizar(@PathVariable Long usuarioId,
                                 @RequestBody @Valid UsuarioInputSemSenha usuarioInputSemSenha) {
        try {
            Usuario usuarioAtual = cadastroUsuarioService.buscarOuFalhar(usuarioId);

            usuarioInputDisassembler.copyToDomainSemSenhaObject(usuarioInputSemSenha, usuarioAtual);

            usuarioAtual = cadastroUsuarioService.salvar(usuarioAtual);

            return usuarioModelAssembler.toModel(usuarioAtual);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long usuarioId) {
        cadastroUsuarioService.excluir(usuarioId);
    }

    @PutMapping("/{usuarioId}/senha")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void redefinirSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInput senhaInput){
        cadastroUsuarioService.redefinirSenha(senhaInput, usuarioId);
    }

}
