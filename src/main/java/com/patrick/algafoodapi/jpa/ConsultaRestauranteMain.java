package com.patrick.algafoodapi.jpa;

import com.patrick.algafoodapi.AlgafoodApiApplication;
import com.patrick.algafoodapi.domain.model.Cozinha;
import com.patrick.algafoodapi.domain.model.Restaurante;
import com.patrick.algafoodapi.infrastructure.repository.CozinhaRepositoryImpl;
import com.patrick.algafoodapi.infrastructure.repository.RestauranteRepositoryImpl;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class ConsultaRestauranteMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        RestauranteRepositoryImpl restaurantes = applicationContext.getBean(RestauranteRepositoryImpl.class);
        List<Restaurante> todosRestaurante = restaurantes.todas();

        for(Restaurante restaurante : todosRestaurante){
            System.out.printf("%s - %f - %s\n",restaurante.getNome(),restaurante.getTaxaFrete(),
                    restaurante.getCozinha().getNome());
        }
    }
}
