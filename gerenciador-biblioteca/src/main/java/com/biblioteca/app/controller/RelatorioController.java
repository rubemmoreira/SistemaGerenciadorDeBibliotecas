package com.biblioteca.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    @GetMapping({"", "/"})
    public String mostrarRelatorios(Model model) {
        System.out.println("=== 📊 ACESSANDO PÁGINA DE RELATÓRIOS ===");
        return "relatorios/dashboard";
    }
}