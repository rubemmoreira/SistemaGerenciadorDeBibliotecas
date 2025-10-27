package com.biblioteca.app.controller;

import com.biblioteca.app.model.Emprestimo;
import com.biblioteca.app.model.Livro;
import com.biblioteca.app.model.Usuario;
import com.biblioteca.app.service.EmprestimoService;
import com.biblioteca.app.service.LivroService;
import com.biblioteca.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {
    
    @Autowired
    private LivroService livroService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private EmprestimoService emprestimoService;
    
    @GetMapping({"", "/"})
    public String dashboardRelatorios(Model model) {
        System.out.println("=== 📊 ACESSANDO DASHBOARD DE RELATÓRIOS ===");
        
        // Estatísticas gerais
        long totalLivros = livroService.findAll().size();
        long totalUsuarios = usuarioService.findAll().size();
        long totalEmprestimos = emprestimoService.findAll().size();
        long emprestimosAtivos = emprestimoService.countEmprestimosAtivos();
        long emprestimosAtrasados = emprestimoService.countEmprestimosAtrasados();
        
        // Livros mais emprestados
        List<Emprestimo> emprestimos = emprestimoService.findAll();
        Map<String, Long> livrosMaisEmprestados = new HashMap<>();
        for (Emprestimo emprestimo : emprestimos) {
            String titulo = emprestimo.getLivro().getTitulo();
            livrosMaisEmprestados.put(titulo, livrosMaisEmprestados.getOrDefault(titulo, 0L) + 1);
        }
        
        // Usuários mais ativos
        Map<String, Long> usuariosMaisAtivos = new HashMap<>();
        for (Emprestimo emprestimo : emprestimos) {
            String nome = emprestimo.getUsuario().getNome();
            usuariosMaisAtivos.put(nome, usuariosMaisAtivos.getOrDefault(nome, 0L) + 1);
        }
        
        model.addAttribute("totalLivros", totalLivros);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalEmprestimos", totalEmprestimos);
        model.addAttribute("emprestimosAtivos", emprestimosAtivos);
        model.addAttribute("emprestimosAtrasados", emprestimosAtrasados);
        model.addAttribute("livrosMaisEmprestados", livrosMaisEmprestados);
        model.addAttribute("usuariosMaisAtivos", usuariosMaisAtivos);
        
        return "relatorios/dashboard";
    }
    
    @GetMapping("/livros")
    public String relatorioLivros(Model model) {
        System.out.println("=== 📖 ACESSANDO RELATÓRIO DE LIVROS ===");
        
        List<Livro> livros = livroService.findAll();
        List<Emprestimo> emprestimos = emprestimoService.findAll();
        
        // Estatísticas por categoria
        Map<String, Long> livrosPorCategoria = new HashMap<>();
        for (Livro livro : livros) {
            String categoria = livro.getCategoria() != null ? livro.getCategoria() : "Sem categoria";
            livrosPorCategoria.put(categoria, livrosPorCategoria.getOrDefault(categoria, 0L) + 1);
        }
        
        // Livros mais emprestados
        Map<String, Long> livrosMaisEmprestados = new HashMap<>();
        for (Emprestimo emprestimo : emprestimos) {
            String titulo = emprestimo.getLivro().getTitulo();
            livrosMaisEmprestados.put(titulo, livrosMaisEmprestados.getOrDefault(titulo, 0L) + 1);
        }
        
        model.addAttribute("livros", livros);
        model.addAttribute("livrosPorCategoria", livrosPorCategoria);
        model.addAttribute("livrosMaisEmprestados", livrosMaisEmprestados);
        
        return "relatorios/livros";
    }
    
    @GetMapping("/usuarios")
    public String relatorioUsuarios(Model model) {
        System.out.println("=== 👥 ACESSANDO RELATÓRIO DE USUÁRIOS ===");
        
        List<Usuario> usuarios = usuarioService.findAll();
        List<Emprestimo> emprestimos = emprestimoService.findAll();
        
        // Usuários mais ativos
        Map<String, Long> usuariosMaisAtivos = new HashMap<>();
        for (Emprestimo emprestimo : emprestimos) {
            String nome = emprestimo.getUsuario().getNome();
            usuariosMaisAtivos.put(nome, usuariosMaisAtivos.getOrDefault(nome, 0L) + 1);
        }
        
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usuariosMaisAtivos", usuariosMaisAtivos);
        
        return "relatorios/usuarios";
    }
    
    @GetMapping("/emprestimos")
    public String relatorioEmprestimos(Model model, 
                                     @RequestParam(required = false) String periodo) {
        System.out.println("=== 🔄 ACESSANDO RELATÓRIO DE EMPRÉSTIMOS ===");
        
        List<Emprestimo> emprestimos = emprestimoService.findAll();
        List<Emprestimo> emprestimosAtivos = emprestimoService.findEmprestimosAtivos();
        List<Emprestimo> emprestimosAtrasados = emprestimoService.findEmprestimosAtrasados();
        
        // Estatísticas por período
        Map<String, Long> emprestimosPorMes = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        
        for (Emprestimo emprestimo : emprestimos) {
            String mes = emprestimo.getDataEmprestimo().format(formatter);
            emprestimosPorMes.put(mes, emprestimosPorMes.getOrDefault(mes, 0L) + 1);
        }
        
        model.addAttribute("emprestimos", emprestimos);
        model.addAttribute("emprestimosAtivos", emprestimosAtivos);
        model.addAttribute("emprestimosAtrasados", emprestimosAtrasados);
        model.addAttribute("emprestimosPorMes", emprestimosPorMes);
        
        return "relatorios/emprestimos";
    }
    
    @GetMapping("/estatisticas")
    public String estatisticasGerais(Model model) {
        System.out.println("=== 📈 ACESSANDO ESTATÍSTICAS GERAIS ===");
        
        // Estatísticas gerais
        long totalLivros = livroService.findAll().size();
        long totalUsuarios = usuarioService.findAll().size();
        long totalEmprestimos = emprestimoService.findAll().size();
        long emprestimosAtivos = emprestimoService.countEmprestimosAtivos();
        long emprestimosAtrasados = emprestimoService.countEmprestimosAtrasados();
        
        // Taxa de devolução
        double taxaDevolucao = totalEmprestimos > 0 ? 
            ((double) emprestimoService.countEmprestimosDevolvidos() / totalEmprestimos) * 100 : 0;
        
        // Média de empréstimos por usuário
        double mediaEmprestimosPorUsuario = totalUsuarios > 0 ? 
            (double) totalEmprestimos / totalUsuarios : 0;
        
        model.addAttribute("totalLivros", totalLivros);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalEmprestimos", totalEmprestimos);
        model.addAttribute("emprestimosAtivos", emprestimosAtivos);
        model.addAttribute("emprestimosAtrasados", emprestimosAtrasados);
        model.addAttribute("taxaDevolucao", taxaDevolucao);
        model.addAttribute("mediaEmprestimosPorUsuario", mediaEmprestimosPorUsuario);
        
        return "relatorios/estatisticas";
    }
}