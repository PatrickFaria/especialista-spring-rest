package com.patrick.algafoodapi.domain.service;

import com.patrick.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.patrick.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.patrick.algafoodapi.domain.exception.RestauranteNaoEncontradoException;
import com.patrick.algafoodapi.domain.model.Cozinha;
import com.patrick.algafoodapi.domain.model.Estado;
import com.patrick.algafoodapi.domain.model.Restaurante;
import com.patrick.algafoodapi.domain.repository.CozinhaRepository;
import com.patrick.algafoodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroRestauranteService {

    public static final String RESTAURANTE_NAO_EXISTENTE = "Não existe um cadastro de restaurante de código %d!";
    public static final String RESTAURANTE_ESTA_EM_USO = "Restaurante de código %d não pode ser removida, pois está em uso!";
    public static final String NÃO_EXISTE_COZINHA_COM_CÓDIGO = "Não existe cadastro de cozinha com código %d!";
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    public Restaurante salvar(Restaurante restaurante){
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
        restaurante.setCozinha(cozinha);
        return restauranteRepository.save(restaurante);
    }

    public void excluir(Long restauranteId){
        try {
            restauranteRepository.deleteById(restauranteId);
        }catch (EmptyResultDataAccessException e){
            throw new RestauranteNaoEncontradoException(
                    String.format(RESTAURANTE_NAO_EXISTENTE, restauranteId)
            );
        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(
                    String.format(RESTAURANTE_ESTA_EM_USO, restauranteId)
            );
        }
    }

    public Restaurante buscarOuFalhar(Long restauranteId) {
        return restauranteRepository.findById(restauranteId).orElseThrow(() ->
                new RestauranteNaoEncontradoException(String.format(RESTAURANTE_NAO_EXISTENTE, restauranteId)
                ));
    }


}
