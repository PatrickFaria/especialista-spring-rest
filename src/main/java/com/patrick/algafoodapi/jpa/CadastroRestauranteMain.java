package com.patrick.algafoodapi.jpa;

import com.patrick.algafoodapi.AlgafoodApiApplication;
import com.patrick.algafoodapi.domain.model.Cozinha;
import com.patrick.algafoodapi.domain.model.Restaurante;
import com.patrick.algafoodapi.infrastructure.repository.CozinhaRepositoryImpl;
import com.patrick.algafoodapi.infrastructure.repository.RestauranteRepositoryImpl;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;

public class CadastroRestauranteMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        RestauranteRepositoryImpl cadastroRestaurante = applicationContext.getBean(RestauranteRepositoryImpl.class);

        BigDecimal bd = new BigDecimal(30.0);
        BigDecimal bd2 = new BigDecimal(12.22);
        Restaurante r1 = new Restaurante();
        r1.setId(1L);
        r1.setNome("Donalds");
        r1.setTaxaFrete(bd);

        Restaurante r2 = new Restaurante();
        r2.setId(2L);
        r2.setNome("Burguer");
        r2.setTaxaFrete(bd2);

        cadastroRestaurante.adicionar(r1);
        cadastroRestaurante.adicionar(r2);

        cadastroRestaurante.remover(r2.getId());
        cadastroRestaurante.adicionar(r1);
    }
}
