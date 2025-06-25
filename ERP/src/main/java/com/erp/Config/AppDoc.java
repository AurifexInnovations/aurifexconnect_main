package com.erp.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class AppDoc {

    /** <a href="http://localhost:8080/swagger-ui/index.html#/">...</a> */

    @Bean
    OpenAPI openAPI(){
        return new OpenAPI().info(info());
    }

    private Info info(){
        return new Info()
                .title("ERP")
                .description("""
                       
                       # Description
                       **ERP** Welcome to the ERP Web Application API documentation. 
                       This application is designed to streamline and automate various business processes such as inventory management, 
                       employee management, order processing, finance, and more. 
                       The application is built using Spring Boot, providing a powerful and scalable solution for managing enterprise resources effectively.
                       
                       # Tech-Stack
                       - Java 8
                       - Spring Boot
                       - Spring Data JPA
                       - MySQL Database
                        """)
                .version("v1")
                .contact(contact());
    }

    private Contact contact() {
        return new Contact().email("aurefix@gmail.com")
                .name("AUREFIX")
                .url("https://google.com");
    }
}
