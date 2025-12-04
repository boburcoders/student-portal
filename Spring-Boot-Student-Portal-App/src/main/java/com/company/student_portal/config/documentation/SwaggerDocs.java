package com.company.student_portal.config.documentation;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerDocs {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot Student Portal")
                        .version("1.0")
                        .description("Spring Boot Student Portal")
                        .termsOfService("https://spring.io/terms")
                        .contact(new Contact()
                                .name("Bobur")
                                .email("boburtoshniyozov4@gmail.com")
                                .url("")))
                .externalDocs(new ExternalDocumentation()
                        .description("Student Portal API Documentation"))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Spring Boot Student Portal Local Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }


    @Bean
    public List<GroupedOpenApi> apis() {
        return List.of(
                GroupedOpenApi.builder()
                        .group("assignments")
                        .pathsToMatch("/api/assignment/**")
                        .build(),

                GroupedOpenApi.builder()
                        .group("auth")
                        .pathsToMatch("/api/auth/**")
                        .build(),

                GroupedOpenApi.builder()
                        .group("course")
                        .pathsToMatch("/api/course/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("department")
                        .pathsToMatch("/api/department/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("enrollment")
                        .pathsToMatch("/api/enrollment/**")
                        .build(),

                GroupedOpenApi.builder()
                        .group("notification")
                        .pathsToMatch("/api/notification/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("program")
                        .pathsToMatch("/api/program/**")
                        .build(),

                GroupedOpenApi.builder()
                        .group("program-enrollment")
                        .pathsToMatch("/api/program-enrollment/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("student-profile")
                        .pathsToMatch("/api/student-profile/**")
                        .build(),

                GroupedOpenApi.builder()
                        .group("submission")
                        .pathsToMatch("/api/submission/**")
                        .build(),

                GroupedOpenApi.builder()
                        .group("teacher-profile")
                        .pathsToMatch("/api/teacher-profile/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("university")
                        .pathsToMatch("/api/university/**")
                        .build()
        );
    }


}
