package com.biblioteca.app.controller;

import com.biblioteca.app.model.Usuario;
import com.biblioteca.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String fazerLogin(
        @RequestParam String username,
        @RequestParam String password,
        Model model
    ) {
        System.out.println("=== 🔍 TENTANDO LOGIN ===");
        System.out.println("Email: " + username);
        System.out.println("Senha: " + password);
        
        // Busca o usuário no banco - retorna Optional
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(username);
        
        // Verifica se o usuário existe e se a senha está correta
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            
            if (usuario.getSenha().equals(password)) {
                System.out.println("✅ LOGIN BEM-SUCEDIDO!");
                System.out.println("Usuário: " + usuario.getNome());
                return "redirect:/livros";
            } else {
                System.out.println("❌ SENHA INCORRETA!");
                model.addAttribute("errorMessage", "Senha incorreta!");
                return "login";
            }
        } else {
            System.out.println("❌ USUÁRIO NÃO ENCONTRADO!");
            model.addAttribute("errorMessage", "Email não encontrado!");
            return "login";
        }
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}