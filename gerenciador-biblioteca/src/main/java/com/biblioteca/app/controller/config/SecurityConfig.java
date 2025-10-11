package com.biblioteca.app.controller.config; // Linha 1: Pacote deve ser o PRIMEIRO

import org.springframework.context.annotation.Bean; // Linha 2
import org.springframework.context.annotation.Configuration; // Linha 3
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // Permite acesso irrestrito ao login e à raiz
                .requestMatchers("/login.html", "/", "/css/**", "/js/**").permitAll()
                // Todas as outras URLs exigem autenticação
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login.html")
                .defaultSuccessUrl("/", true) // Redireciona para o index.html
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/login.html")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}