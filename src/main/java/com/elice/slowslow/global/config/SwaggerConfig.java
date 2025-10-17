package com.elice.slowslow.global.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Slowslow API Docs")
                        .description("스프링부트 백엔드 API 명세서")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("김연지")
                                .email("your_email@example.com")
                                .url("https://github.com/JWhy121")
                        )
                );
    }
}
