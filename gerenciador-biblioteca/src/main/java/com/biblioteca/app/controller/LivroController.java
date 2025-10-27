package com.biblioteca.app.controller;

import com.biblioteca.app.model.Livro;
import com.biblioteca.app.model.Usuario;
import com.biblioteca.app.service.LivroService;
import com.biblioteca.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"", "/"})
    public String listarLivros(Model model) {
        // Buscar usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioLogado = auth.getName();
        
        Usuario usuarioLogado = usuarioService.findByEmail(emailUsuarioLogado)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + emailUsuarioLogado));
        
        model.addAttribute("livros", livroService.findAll());
        model.addAttribute("livro", new Livro());
        model.addAttribute("usuarioLogado", usuarioLogado);
        return "livros/lista";
    }

    @GetMapping("/cadastro")
    public String formularioCadastro(Model model) {
        model.addAttribute("livro", new Livro());
        return "livros/cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarLivro(@ModelAttribute Livro livro, RedirectAttributes redirectAttributes) {
        if (!livroService.validarTitulo(livro.getTitulo())) {
            redirectAttributes.addFlashAttribute("error", "Título deve ter pelo menos 2 caracteres!");
            return "redirect:/livros/cadastro";
        }
        
        if (!livroService.validarAutor(livro.getAutor())) {
            redirectAttributes.addFlashAttribute("error", "Autor deve ter pelo menos 2 caracteres!");
            return "redirect:/livros/cadastro";
        }
        
        if (!livroService.validarQuantidade(livro.getQuantidade())) {
            redirectAttributes.addFlashAttribute("error", "Quantidade deve ser maior ou igual a zero!");
            return "redirect:/livros/cadastro";
        }
        
        if (!livroService.validarAnoPublicacao(livro.getAnoPublicacao())) {
            redirectAttributes.addFlashAttribute("error", "Ano de publicação inválido!");
            return "redirect:/livros/cadastro";
        }
        
        if (!livroService.validarIsbn(livro.getIsbn())) {
            redirectAttributes.addFlashAttribute("error", "ISBN inválido! Deve ter 10 ou 13 dígitos.");
            return "redirect:/livros/cadastro";
        }
        
        if (livroService.isbnExiste(livro.getIsbn())) {
            redirectAttributes.addFlashAttribute("error", "ISBN já existe no sistema!");
            return "redirect:/livros/cadastro";
        }
        
        try {
            livroService.save(livro);
            redirectAttributes.addFlashAttribute("success", "Livro cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao cadastrar livro: " + e.getMessage());
        }
        
        return "redirect:/livros";
    }

    @GetMapping("/editar/{id}")
    public String formularioEdicao(@PathVariable Long id, Model model) {
        Optional<Livro> livro = livroService.findById(id);
        if (livro.isPresent()) {
            model.addAttribute("livro", livro.get());
            return "livros/cadastro";
        } else {
            return "redirect:/livros";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarLivro(@PathVariable Long id, @ModelAttribute Livro livro, RedirectAttributes redirectAttributes) {
        if (!livroService.validarTitulo(livro.getTitulo())) {
            redirectAttributes.addFlashAttribute("error", "Título deve ter pelo menos 2 caracteres!");
            return "redirect:/livros/editar/" + id;
        }
        
        if (!livroService.validarAutor(livro.getAutor())) {
            redirectAttributes.addFlashAttribute("error", "Autor deve ter pelo menos 2 caracteres!");
            return "redirect:/livros/editar/" + id;
        }
        
        if (!livroService.validarQuantidade(livro.getQuantidade())) {
            redirectAttributes.addFlashAttribute("error", "Quantidade deve ser maior ou igual a zero!");
            return "redirect:/livros/editar/" + id;
        }
        
        if (!livroService.validarAnoPublicacao(livro.getAnoPublicacao())) {
            redirectAttributes.addFlashAttribute("error", "Ano de publicação inválido!");
            return "redirect:/livros/editar/" + id;
        }
        
        if (!livroService.validarIsbn(livro.getIsbn())) {
            redirectAttributes.addFlashAttribute("error", "ISBN inválido! Deve ter 10 ou 13 dígitos.");
            return "redirect:/livros/editar/" + id;
        }
        
        if (livroService.isbnExisteParaOutroLivro(livro.getIsbn(), id)) {
            redirectAttributes.addFlashAttribute("error", "ISBN já existe para outro livro!");
            return "redirect:/livros/editar/" + id;
        }
        
        try {
            livro.setId(id);
            livroService.save(livro);
            redirectAttributes.addFlashAttribute("success", "Livro editado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao editar livro: " + e.getMessage());
        }
        
        return "redirect:/livros";
    }

    @GetMapping("/deletar/{id}")
    public String deletarLivro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!livroService.podeDeletarLivro(id)) {
            redirectAttributes.addFlashAttribute("error", "Não é possível deletar este livro pois ele possui empréstimos ativos!");
            return "redirect:/livros";
        }
        
        try {
            livroService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Livro deletado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao deletar livro: " + e.getMessage());
        }
        return "redirect:/livros";
    }

    @GetMapping("/visualizar/{id}")
    public String visualizarLivro(@PathVariable Long id, Model model) {
        Optional<Livro> livro = livroService.findById(id);
        if (livro.isPresent()) {
            model.addAttribute("livro", livro.get());
            return "livros/visualizar";
        } else {
            return "redirect:/livros";
        }
    }
    
    @GetMapping("/buscar")
    public String buscarLivros(@RequestParam(required = false) String termo, Model model) {
        // Buscar usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioLogado = auth.getName();
        
        Usuario usuarioLogado = usuarioService.findByEmail(emailUsuarioLogado)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + emailUsuarioLogado));
        
        if (termo != null && !termo.trim().isEmpty()) {
            model.addAttribute("livros", livroService.buscarPorTermo(termo));
            model.addAttribute("termoBusca", termo);
        } else {
            model.addAttribute("livros", livroService.findAll());
        }
        model.addAttribute("usuarioLogado", usuarioLogado);
        return "livros/lista";
    }
    
    @GetMapping("/buscar/titulo")
    public String buscarPorTitulo(@RequestParam String titulo, Model model) {
        // Buscar usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioLogado = auth.getName();
        
        Usuario usuarioLogado = usuarioService.findByEmail(emailUsuarioLogado)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + emailUsuarioLogado));
        
        model.addAttribute("livros", livroService.buscarPorTitulo(titulo));
        model.addAttribute("filtroAtivo", "Título: " + titulo);
        model.addAttribute("usuarioLogado", usuarioLogado);
        return "livros/lista";
    }
    
    @GetMapping("/buscar/autor")
    public String buscarPorAutor(@RequestParam String autor, Model model) {
        // Buscar usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioLogado = auth.getName();
        
        Usuario usuarioLogado = usuarioService.findByEmail(emailUsuarioLogado)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + emailUsuarioLogado));
        
        model.addAttribute("livros", livroService.buscarPorAutor(autor));
        model.addAttribute("filtroAtivo", "Autor: " + autor);
        model.addAttribute("usuarioLogado", usuarioLogado);
        return "livros/lista";
    }
    
    @GetMapping("/buscar/categoria")
    public String buscarPorCategoria(@RequestParam String categoria, Model model) {
        // Buscar usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioLogado = auth.getName();
        
        Usuario usuarioLogado = usuarioService.findByEmail(emailUsuarioLogado)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + emailUsuarioLogado));
        
        model.addAttribute("livros", livroService.buscarPorCategoria(categoria));
        model.addAttribute("filtroAtivo", "Categoria: " + categoria);
        model.addAttribute("usuarioLogado", usuarioLogado);
        return "livros/lista";
    }
}
