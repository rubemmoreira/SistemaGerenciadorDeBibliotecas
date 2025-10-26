package com.biblioteca.app.controller;

import com.biblioteca.app.model.Usuario;
import com.biblioteca.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.List;

@RestController
public class DatabaseTestController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/test-mysql")
    public String testMySQL() {
        StringBuilder result = new StringBuilder();
        
        try {
            // Teste 1: Conexão básica
            result.append("=== 🔌 TESTE DE CONEXÃO MYSQL ===<br>");
            
            // Teste 2: Verificar se pode conectar
            String database = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
            result.append("✅ Conectado ao banco: ").append(database).append("<br>");
            
            // Teste 3: Verificar tabelas
            List<String> tables = jdbcTemplate.queryForList(
                "SHOW TABLES", String.class);
            result.append("✅ Tabelas no banco: ").append(tables).append("<br>");
            
            // Teste 4: Verificar usuários
            List<Usuario> usuarios = usuarioRepository.findAll();
            result.append("✅ Total de usuários: ").append(usuarios.size()).append("<br>");
            
            // Teste 5: Verificar usuário específico
            var usuario = usuarioRepository.findByEmail("admin@biblioteca.com");
            if (usuario.isPresent()) {
                result.append("✅ Usuário admin encontrado: ")
                      .append(usuario.get().getEmail())
                      .append(" | ")
                      .append(usuario.get().getSenha())
                      .append(" | ")
                      .append(usuario.get().getNome())
                      .append("<br>");
            } else {
                result.append("❌ Usuário admin NÃO encontrado<br>");
            }
            
            return result.toString();
            
        } catch (Exception e) {
            return "❌ ERRO na conexão MySQL: " + e.getMessage();
        }
    }
}