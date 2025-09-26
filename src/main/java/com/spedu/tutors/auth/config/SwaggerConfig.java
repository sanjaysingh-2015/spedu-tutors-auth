package com.spedu.tutors.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {
    @Bean
    public OpenAPI springAuthAPI() {
        return new OpenAPI()
                .info(new Info().title("Auth API")
                        .description("Authentication service with JWT")
                        .version("v1.0"));
    }
}
