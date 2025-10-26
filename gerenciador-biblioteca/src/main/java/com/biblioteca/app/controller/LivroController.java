package com.biblioteca.app.controller;

import com.biblioteca.app.model.Livro;
import com.biblioteca.app.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping({"", "/"})
    public String listarLivros(Model model) {
        System.out.println("=== 🚀 ACESSANDO PÁGINA DE LIVROS ===");
        List<Livro> livros = livroService.findAll();
        System.out.println("Total de livros: " + livros.size());
        
        model.addAttribute("livros", livros);
        model.addAttribute("livro", new Livro());
        return "livros/lista";
    }

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        System.out.println("=== 📝 ACESSANDO FORMULÁRIO DE CADASTRO ===");
        model.addAttribute("livro", new Livro());
        return "livros/cadastro";
    }

    @PostMapping("/cadastrar")
    public String cadastrarLivro(@ModelAttribute Livro livro, Model model) {
        System.out.println("=== 💾 TENTANDO CADASTRAR LIVRO ===");
        
        try {
            // Apenas validações básicas - sem usar campos extras
            if (livro.getTitulo() == null || livro.getTitulo().trim().isEmpty()) {
                throw new RuntimeException("Título é obrigatório");
            }
            if (livro.getAutor() == null || livro.getAutor().trim().isEmpty()) {
                throw new RuntimeException("Autor é obrigatório");
            }
            
            livroService.save(livro);
            System.out.println("✅ LIVRO CADASTRADO COM SUCESSO!");
            return "redirect:/livros?success";
            
        } catch (Exception e) {
            System.out.println("❌ ERRO AO CADASTRAR LIVRO: " + e.getMessage());
            model.addAttribute("errorMessage", "Erro ao cadastrar livro: " + e.getMessage());
            return "livros/cadastro";
        }
    }
}