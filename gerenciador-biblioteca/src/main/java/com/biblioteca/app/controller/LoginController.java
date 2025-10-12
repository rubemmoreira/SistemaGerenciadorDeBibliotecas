package com.biblioteca.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Mapeia a URL raiz "/" para o template "index" (página principal)
    @GetMapping("/")
    public String index() {
        return "index"; 
    }

    // Mapeia a URL de login para o template "login"
    @GetMapping("/login") // Mapeamento corrigido
    public String login() {
        return "login"; // Retorna o nome do template: login.html
    }
}
