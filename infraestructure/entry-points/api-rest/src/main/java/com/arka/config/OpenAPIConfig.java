package com.arka.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.description("Open API Documentation Local");


        Contact contact = new Contact();
        contact.name("Arka Enjoi");
        contact.email("arka@yopmail.com");

        Info info = new Info()
                .title("Arka Enjoi")
                .description("Proyecto de autenticacion del servicios ARKA")
                .contact(contact)
                .version("1.0")
                .license(new License().name("Apache 2.0").url("http://localhost"));


        return new OpenAPI().info(info).servers(List.of(server));



    }

}
