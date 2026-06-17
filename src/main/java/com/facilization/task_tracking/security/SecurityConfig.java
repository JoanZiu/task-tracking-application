package com.facilization.task_tracking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // lejo Swagger dhe H2 console pa login
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
                        // lejo krijimin e user-it pa login (qe te mund te regjistrohesh)
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/users").permitAll()
                        // gjithcka tjeter kerkon login
                        .anyRequest().authenticated()
                )
                .httpBasic(org.springframework.security.config.Customizer.withDefaults())
                // e nevojshme qe H2 console te funksionoje
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}