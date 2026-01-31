package com.tuproyecto.inventory.infra.security;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JTW")))
                .info(new Info()
                        .title("API Inventory")
                        .description("API Rest de la aplicación Inventory, que contiene las funcionalidades de CRUD de productos y usuarios.")
                        .contact(new Contact()
                                .name("Jonathan Dev")
                                .email("jonathandiaz.dev@gmail.com")));
    }
}
