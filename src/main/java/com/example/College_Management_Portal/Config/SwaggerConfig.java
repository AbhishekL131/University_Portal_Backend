package com.example.College_Management_Portal.Config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;


@Configuration
public class SwaggerConfig {
    

    @Bean
    public OpenAPI myCustomConfig(){
        return new OpenAPI().info(
            new Info().title("University Management ERP")
            .description("By Abhishek")
        )
         .servers(Arrays.asList(new Server().url("http://localhost:8080").description("Local")))
         .tags(Arrays.asList(
            new Tag().name("Login APIs"),
            new Tag().name("Admin APIs"),
            new Tag().name("Faculty APIs"),
            new Tag().name("Student APIs"),
            new Tag().name("Course APIs"),
            new Tag().name("Department APIs"),
            new Tag().name("Faculty-Student APIs"),
            new Tag().name("Faculty-Course APIs"),
            new Tag().name("Student-Course APIs")
         ));
    }
}
