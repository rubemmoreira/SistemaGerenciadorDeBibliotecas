package com.biblioteca.app.controller;

import com.biblioteca.app.model.Usuario;
import com.biblioteca.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Mapeia a URL inicial para a tela de login.
     */
    @GetMapping("/")
    public String exibirLogin() {
        return "login"; // Retorna o template 'login.html'
    }

    /**
     * Recebe e processa a requisição POST do formulário de login.
     * @param username - Captura o campo 'username' do HTML (o email)
     * @param password - Captura o campo 'password' do HTML (a senha)
     */
    @PostMapping("/login")
    public String fazerLogin(
            @RequestParam String username,
            @RequestParam String password) {

        // 1. Busca o usuário no banco de dados pelo email
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(username);

        // 2. Verifica se o usuário existe e se a senha confere
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // ATENÇÃO: Em projetos reais, use BCrypt para criptografar a senha!
            if (usuario.getSenha().equals(password)) {
                
                // Login OK: Redireciona para a página principal (lista-livros)
                // Você pode adicionar a lógica de Sessão (ex: Spring Security) aqui.
                return "redirect:/livros"; 
            }
        }

        // Login Falhou: Redireciona de volta para a tela de login com o parâmetro 'error'
        return "redirect:/?error";
    }
    
    /**
     * Endpoint de destino em caso de login bem-sucedido.
     */
    @GetMapping("/livros")
    public String listaLivros() {
        return "lista-livros"; // Retorna o template 'lista-livros.html'
    }
}