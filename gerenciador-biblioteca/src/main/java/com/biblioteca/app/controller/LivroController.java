package com.biblioteca.app.controller;

import com.biblioteca.app.model.Livro;
import com.biblioteca.app.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping({"", "/"})
    public String listarLivros(Model model) {
        System.out.println("=== 🚀 ACESSANDO PÁGINA DE LIVROS ===");
        model.addAttribute("livros", livroService.findAll());
        model.addAttribute("livro", new Livro());
        return "livros/lista";
    }

    // ... outros métodos
}