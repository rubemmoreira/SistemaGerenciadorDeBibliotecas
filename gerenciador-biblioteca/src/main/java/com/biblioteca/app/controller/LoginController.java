package com.biblioteca.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Mapeia a URL raiz "/" para o template "index" (que corresponde a index.html)
    @GetMapping("/")
    public String index() {
        return "index"; // Retorna o nome do template: index.html
    }

    // Mapeia a URL de login para o template "login" (que corresponde a login.html)
    @GetMapping("/login.html")
    public String login() {
        return "login"; // Retorna o nome do template: login.html
    }
}