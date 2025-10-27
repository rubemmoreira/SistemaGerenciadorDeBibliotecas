package com.biblioteca.app.controller;

import com.biblioteca.app.model.Usuario;
import com.biblioteca.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String loginPage(
        @RequestParam(value = "error", required = false) String error,
        @RequestParam(value = "logout", required = false) String logout,
        Model model
    ) {
        if (error != null) {
            model.addAttribute("errorMessage", "Email ou senha inválidos!");
        }
        
        if (logout != null) {
            model.addAttribute("successMessage", "Logout realizado com sucesso!");
        }
        
        return "login";
    }

    @GetMapping("/home")
    public String homePage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<Usuario> usuario = usuarioService.findByEmail(email);
            if (usuario.isPresent()) {
                model.addAttribute("usuarioLogado", usuario.get());
            }
        }
        
        return "index";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
}