package com.biblioteca.app.config;

import com.biblioteca.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioService usuarioService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("=== 🔧 CONFIGURANDO SEGURANÇA ===");
        
        http
            .authorizeHttpRequests(authz -> authz
                // Permite acesso público a recursos estáticos e endpoints de teste
                .requestMatchers(
                    "/css/**", "/js/**", "/images/**", "/webjars/**",
                    "/login", 
                    "/test-db", "/test-user", "/test-mysql", "/test-connection"  // ← ADICIONE ESTES
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .userDetailsService(usuarioService);

        http.csrf(csrf -> csrf.disable());
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                System.out.println("🔐 Codificando senha: " + rawPassword);
                return rawPassword.toString();
            }
            
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                System.out.println("🔍 Comparando senhas:");
                System.out.println("   Senha digitada: " + rawPassword);
                System.out.println("   Senha no banco: " + encodedPassword);
                boolean match = rawPassword.toString().equals(encodedPassword);
                System.out.println("   Resultado: " + (match ? "✅ CORRETO" : "❌ INCORRETO"));
                return match;
            }
        };
    }
}