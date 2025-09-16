package com.servicemarketplace.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {

        return new OpenAPI()
            .info(new Info().title("Service-MarketPlace API"))
            .addSecurityItem(new SecurityRequirement().addList("Service-MarketplaceSecurityScheme"))
            .components(new Components().addSecuritySchemes("Service-MarketplaceSecurityScheme", new SecurityScheme()
                .name("Service-MarketplaceSecurityScheme").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
