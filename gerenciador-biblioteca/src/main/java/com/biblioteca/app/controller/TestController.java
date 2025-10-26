package com.biblioteca.app.controller;

import com.biblioteca.app.model.Usuario;
import com.biblioteca.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/test-db")
    public String testDB() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            return "Conexão MySQL OK! Usuários no banco: " + usuarios.size();
        } catch (Exception e) {
            return "ERRO na conexão MySQL: " + e.getMessage();
        }
    }

    @GetMapping("/test-user")
    public String testUser() {
        try {
            var usuario = usuarioRepository.findByEmail("admin@biblioteca.com");
            if (usuario.isPresent()) {
                return "✅ Usuário encontrado: " + usuario.get().getNome() + " | Senha: " + usuario.get().getSenha();
            } else {
                return "❌ Usuário NÃO encontrado no MySQL";
            }
        } catch (Exception e) {
            return "ERRO ao buscar usuário: " + e.getMessage();
        }
    }

    @GetMapping("/test-connection")
    public String testConnection() {
        return "✅ Aplicação rodando - Teste de conexão MySQL";
    }
}