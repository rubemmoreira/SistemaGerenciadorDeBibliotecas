package com.biblioteca.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(
        @RequestParam(value = "error", required = false) String error,
        @RequestParam(value = "logout", required = false) String logout,
        Model model
    ) {
        System.out.println("=== 🔐 ACESSANDO PÁGINA DE LOGIN ===");
        
        if (error != null) {
            model.addAttribute("errorMessage", "Email ou senha inválidos!");
            System.out.println("❌ Tentativa de login com erro");
        }
        
        if (logout != null) {
            model.addAttribute("successMessage", "Logout realizado com sucesso!");
            System.out.println("✅ Logout realizado");
        }
        
        return "login";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        System.out.println("=== 🏠 ACESSANDO PÁGINA HOME ===");
        System.out.println("📁 Tentando carregar: src/main/resources/templates/index.html");
        return "index";
    }

    @GetMapping("/")
    public String index() {
        System.out.println("=== 🏠 REDIRECIONANDO PARA HOME ===");
        return "redirect:/home";
    }
}