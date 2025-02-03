package com.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SwaggerConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for development/testing
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/swagger-ui/**", 
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/api/user"  // Allow test API
                ).permitAll()  // Allow public access to Swagger UI
                .anyRequest().authenticated()  // Secure all other endpoints
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
