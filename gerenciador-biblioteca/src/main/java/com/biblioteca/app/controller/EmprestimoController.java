package com.biblioteca.app.controller;

import com.biblioteca.app.model.Emprestimo;
import com.biblioteca.app.model.Usuario;
import com.biblioteca.app.service.EmprestimoService;
import com.biblioteca.app.service.LivroService;
import com.biblioteca.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {
    
    @Autowired
    private EmprestimoService emprestimoService;
    
    @Autowired
    private LivroService livroService;
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"", "/"})
    public String listarEmprestimos(Model model) {
        try {
            // Buscar usuário logado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String emailUsuarioLogado = auth.getName();
            
            Usuario usuarioLogado = usuarioService.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + emailUsuarioLogado));
            
            // Buscar empréstimos baseado no role
            List<Emprestimo> emprestimos;
            if (usuarioLogado.isUsuario()) {
                emprestimos = emprestimoService.findByUsuarioId(usuarioLogado.getId());
            } else {
                emprestimos = emprestimoService.findAll();
            }
            
            // Adicionar atributos ao modelo
            model.addAttribute("emprestimos", emprestimos);
            model.addAttribute("usuarioLogado", usuarioLogado);
            
            return "emprestimos/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar empréstimos: " + e.getMessage());
            model.addAttribute("emprestimos", List.of());
            return "emprestimos/lista";
        }
    }

    @GetMapping("/novo")
    public String formularioNovoEmprestimo(Model model) {
        model.addAttribute("emprestimo", new Emprestimo());
        model.addAttribute("livros", livroService.findAll());
        model.addAttribute("usuarios", usuarioService.findAll());
        return "emprestimos/novo";
    }

    @PostMapping("/novo")
    public String criarEmprestimo(@RequestParam Long livroId, 
                                   @RequestParam Long usuarioId,
                                   @RequestParam(defaultValue = "7") Integer diasEmprestimo,
                                   RedirectAttributes redirectAttributes) {
        
        try {
            emprestimoService.realizarEmprestimo(livroId, usuarioId, diasEmprestimo);
            redirectAttributes.addFlashAttribute("success", "Empréstimo realizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao criar empréstimo: " + e.getMessage());
        }
        
        return "redirect:/emprestimos";
    }

    @GetMapping("/devolver/{id}")
    public String devolverLivro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            emprestimoService.devolverLivro(id);
            redirectAttributes.addFlashAttribute("success", "Livro devolvido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao devolver livro: " + e.getMessage());
        }
        return "redirect:/emprestimos";
    }

    @GetMapping("/editar/{id}")
    public String formularioEdicao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Emprestimo> emprestimo = emprestimoService.findById(id);
        if (emprestimo.isPresent()) {
            model.addAttribute("emprestimo", emprestimo.get());
            model.addAttribute("livros", livroService.findAll());
            model.addAttribute("usuarios", usuarioService.findAll());
            return "emprestimos/editar";
        } else {
            redirectAttributes.addFlashAttribute("error", "Empréstimo não encontrado!");
            return "redirect:/emprestimos";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarEmprestimo(@PathVariable Long id, 
                                    @RequestParam Long livroId,
                                    @RequestParam Long usuarioId,
                                    @RequestParam String status,
                                    @RequestParam String dataEmprestimo,
                                    @RequestParam String dataDevolucaoPrevista,
                                    @RequestParam(required = false) String dataDevolucaoReal,
                                    @RequestParam(required = false) String observacoes,
                                    RedirectAttributes redirectAttributes) {
        try {
            emprestimoService.atualizarEmprestimo(id, livroId, usuarioId, status, 
                                                   dataEmprestimo, dataDevolucaoPrevista, 
                                                   dataDevolucaoReal, observacoes);
            redirectAttributes.addFlashAttribute("success", "Empréstimo editado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao editar empréstimo: " + e.getMessage());
        }
        return "redirect:/emprestimos";
    }

    @GetMapping("/deletar/{id}")
    public String deletarEmprestimo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            emprestimoService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Empréstimo deletado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao deletar empréstimo: " + e.getMessage());
        }
        return "redirect:/emprestimos";
    }

    @GetMapping("/ativos")
    public String listarEmprestimosAtivos(Model model) {
        try {
            // Buscar usuário logado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String emailUsuarioLogado = auth.getName();
            
            Usuario usuarioLogado = usuarioService.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + emailUsuarioLogado));
            
            model.addAttribute("emprestimos", emprestimoService.findEmprestimosAtivos());
            model.addAttribute("titulo", "Empréstimos Ativos");
            model.addAttribute("usuarioLogado", usuarioLogado);
            return "emprestimos/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar empréstimos ativos: " + e.getMessage());
            model.addAttribute("emprestimos", List.of());
            return "emprestimos/lista";
        }
    }

    @GetMapping("/visualizar/{id}")
    public String visualizarEmprestimo(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Emprestimo> emprestimo = emprestimoService.findById(id);
        if (emprestimo.isPresent()) {
            model.addAttribute("emprestimo", emprestimo.get());
            return "emprestimos/visualizar";
        } else {
            redirectAttributes.addFlashAttribute("error", "Empréstimo não encontrado!");
            return "redirect:/emprestimos";
        }
    }

    @GetMapping("/atrasados")
    public String listarEmprestimosAtrasados(Model model) {
        try {
            // Buscar usuário logado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String emailUsuarioLogado = auth.getName();
            
            Usuario usuarioLogado = usuarioService.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + emailUsuarioLogado));
            
            model.addAttribute("emprestimos", emprestimoService.findEmprestimosAtrasados());
            model.addAttribute("titulo", "Empréstimos Atrasados");
            model.addAttribute("usuarioLogado", usuarioLogado);
            return "emprestimos/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar empréstimos atrasados: " + e.getMessage());
            model.addAttribute("emprestimos", List.of());
            return "emprestimos/lista";
        }
    }
    
}