package com.example.travelday.global.config.security;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${ClientURL}")
    private String clientURL;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping(("/**"))
                    .allowCredentials(true)
                    .allowedOrigins(clientURL)
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .maxAge(3600);
            }
        };
    }
}
