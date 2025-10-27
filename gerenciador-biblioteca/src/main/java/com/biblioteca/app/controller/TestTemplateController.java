package com.biblioteca.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestTemplateController {

    @GetMapping("/test-template")
    public String testTemplate(Model model) {
        System.out.println("=== 🧪 TESTANDO TEMPLATE THYMELEAF ===");
        model.addAttribute("mensagem", "Thymeleaf está funcionando!");
        return "index"; // ← Tenta carregar o mesmo index.html
    }
}