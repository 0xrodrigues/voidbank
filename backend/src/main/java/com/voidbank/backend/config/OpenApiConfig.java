package com.voidbank.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI voidBankOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VoidBank API")
                        .description("API para gerenciamento de contas bancárias e transações do VoidBank")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("VoidBank Team")
                                .email("contact@voidbank.com")
                                .url("https://github.com/0xrodrigues/voidbank"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.voidbank.com")
                                .description("Servidor de Produção")
                ));
    }
}
