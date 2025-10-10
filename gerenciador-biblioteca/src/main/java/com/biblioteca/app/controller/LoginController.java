package com.biblioteca.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Retorna o template login.html
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // Página após login bem-sucedido
    }
}