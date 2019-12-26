package com.patrick.algafoodapi.jpa;

import com.patrick.algafoodapi.AlgafoodApiApplication;
import com.patrick.algafoodapi.domain.model.Cozinha;
import com.patrick.algafoodapi.infrastructure.repository.CozinhaRepositoryImpl;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class ExclusaoCozinhaMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        CozinhaRepositoryImpl cozinhas = applicationContext.getBean(CozinhaRepositoryImpl.class);

        Cozinha cozinha = new Cozinha();
        cozinha.setId(1L);

        cozinhas.remover(cozinha);

    }
}
