package com.bankingapplication.account_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow requests from frontend origin (use pattern for Spring Boot 3.x compatibility)
        config.addAllowedOriginPattern("http://localhost:3000");

        // Allow all headers
        config.addAllowedHeader("*");

        // Allow all methods (GET, POST, PUT, DELETE, OPTIONS)
        config.addAllowedMethod("*");

        // Allow credentials (cookies, authorization headers, etc)
        config.setAllowCredentials(true);

        // Apply this configuration to all paths
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
