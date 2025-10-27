package com.biblioteca.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @GetMapping({"", "/"})
    public String listarEmprestimos(Model model) {
        System.out.println("=== 🔄 ACESSANDO PÁGINA DE EMPRÉSTIMOS ===");
        return "emprestimos/lista";
    }
}