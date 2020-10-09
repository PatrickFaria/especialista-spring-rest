package com.patrick.algafoodapi.domain.service;

import com.patrick.algafoodapi.api.model.input.SenhaInput;
import com.patrick.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.patrick.algafoodapi.domain.exception.NegocioException;
import com.patrick.algafoodapi.domain.exception.UsuarioNaoEncontradoException;
import com.patrick.algafoodapi.domain.model.Usuario;
import com.patrick.algafoodapi.domain.repository.UsuarioRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroUsuarioService {

    public static final String USUARIO_EM_USO = "Usuario de código %d não pode ser removido, pois está em uso!";
    public static final String SENHA_ATUAL_INCORRETA = "Senha atual informada não coincide com a senha do usuário";

    @Autowired
    private UsuarioRespository usuarioRespository;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        return usuarioRespository.save(usuario);
    }

    @Transactional
    public void excluir(Long usuarioId) {
        try {
            usuarioRespository.deleteById(usuarioId);
            usuarioRespository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new UsuarioNaoEncontradoException(usuarioId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(USUARIO_EM_USO, usuarioId)
            );
        }
    }

    public Usuario buscarOuFalhar(Long usuarioId) {
        return usuarioRespository.findById(usuarioId).orElseThrow(() ->
                new UsuarioNaoEncontradoException(usuarioId));
    }

    @Transactional
    public void redefinirSenha(SenhaInput senhaInput, Long usuarioId) {
        Usuario usuario = buscarOuFalhar(usuarioId);
        if (usuario.senhaNaoCoincideCom(senhaInput.getSenhaAtual())) {
            throw new NegocioException(SENHA_ATUAL_INCORRETA);
        }
        usuario.setSenha(senhaInput.getNovaSenha());
    }



}
