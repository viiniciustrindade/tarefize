package com.senai.todolist.infraecstruture.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Lista de Tarefas")
                        .version("1.0.0")
                        .description("""
                                Esta é uma API de gerenciamento de tarefas (ToDoList) desenvolvida com Spring Boot, seguindo as melhores práticas de desenvolvimento, arquitetura limpa (Clean Architecture) e alguns princípios de DDD (Domain-Driven Design).
                                A aplicação permite que usuários autenticados criem, atualizem, consultem e excluam tarefas, atribuindo prioridades e mantendo o controle completo sobre suas atividades.
                                A API também está preparada para integrações futuras, incluindo autenticação via OAuth2 com provedores externos, testes unitários, de integração e end-to-end, garantindo qualidade, manutenibilidade e escalabilidade do sistema.""")
                        .contact(new Contact()
                                .name("Vinicius Trindade")
                                .email("vinicius.mjt@gmail.com"))
                        .license(new License()
                                .name("Licença MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
