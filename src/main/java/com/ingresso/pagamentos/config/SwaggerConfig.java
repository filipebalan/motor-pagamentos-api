package com.ingresso.pagamentos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI motorPagamentosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Motor de Pagamentos - Alta Concorrência")
                        .description("API robusta para processamento assíncrono de transferências financeiras, protegida contra condições de corrida.")
                        .version("v1.0.0"));
    }
}