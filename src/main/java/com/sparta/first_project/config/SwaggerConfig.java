package com.sparta.first_project.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "항해 뮤직 API 명세서",
                description = "항해 뮤직 서비스 API 명세서",
                version = "v3"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {


}
