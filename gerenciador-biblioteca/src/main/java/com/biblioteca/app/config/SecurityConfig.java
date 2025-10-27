package com.biblioteca.app.config;

import com.biblioteca.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioService usuarioService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(
                    "/css/**", "/js/**", "/images/**", "/webjars/**",
                    "/login", "/home"
                ).permitAll()
                // Usuários - apenas ADMIN pode gerenciar, BIBLIOTECARIO pode ver/criar/editar (não deletar)
                .requestMatchers("/usuarios/deletar/**").hasRole("ADMIN")
                .requestMatchers("/usuarios/**").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                // Livros - todos podem ver, apenas ADMIN/BIBLIOTECARIO podem gerenciar
                .requestMatchers("/livros/cadastro", "/livros/editar/**", "/livros/deletar/**").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                .requestMatchers("/livros/**").authenticated()
                // Empréstimos - todos podem ver seus próprios, apenas ADMIN/BIBLIOTECARIO podem gerenciar
                .requestMatchers("/emprestimos/novo", "/emprestimos/editar/**", "/emprestimos/devolver/**", "/emprestimos/deletar/**").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                .requestMatchers("/emprestimos/**").authenticated()
                // Relatórios - apenas ADMIN/BIBLIOTECARIO
                .requestMatchers("/relatorios/**").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .and()
                .sessionFixation().migrateSession()
                .invalidSessionUrl("/login?expired=true")
            )
            .csrf(csrf -> csrf.disable())
            .userDetailsService(usuarioService);

        return http.build();
    }

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }
}
