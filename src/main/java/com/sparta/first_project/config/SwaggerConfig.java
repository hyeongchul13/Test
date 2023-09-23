package com.sparta.first_project.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(title = "항해 뮤직 API 명세서",
                description = "항해 뮤직 서비스 API 명세서",
                version = "v3"))
@RequiredArgsConstructor
//리프레쉬 토큰용
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        String key = "Access Token (Bearer)";
        String refreshKey = "Refresh Token";

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(key)
                .addList(refreshKey);

        SecurityScheme accessTokenSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer") //
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Access_Token");

        SecurityScheme refreshTokenSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Refresh_Token");

        Components components = new Components()
                .addSecuritySchemes(key, accessTokenSecurityScheme)
                .addSecuritySchemes(refreshKey, refreshTokenSecurityScheme);

        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info().title("항해 뮤직 API 명세서")
                        .description("항해 뮤직 서비스 API 명세서")
                        .version("v3"))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}



