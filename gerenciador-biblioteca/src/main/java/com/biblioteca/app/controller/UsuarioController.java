package com.biblioteca.app.controller;

import com.biblioteca.app.model.Usuario;
import com.biblioteca.app.model.TipoUsuario;
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
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping({"", "/"})
    public String listarUsuarios(Model model, Authentication authentication) {
        String emailUsuarioLogado = authentication.getName();
        Optional<Usuario> usuarioLogadoOpt = usuarioService.findByEmail(emailUsuarioLogado);
        
        if (!usuarioLogadoOpt.isPresent()) {
            return "redirect:/login";
        }
        
        Usuario usuarioLogado = usuarioLogadoOpt.get();
        List<Usuario> usuarios;
        
        // Filtrar usuários baseado no role
        if (usuarioLogado.isAdmin()) {
            // Admin vê todos os usuários
            usuarios = usuarioService.findAll();
        } else if (usuarioLogado.isBibliotecario()) {
            // Bibliotecário vê apenas usuários comuns
            usuarios = usuarioService.findByTipoUsuario(TipoUsuario.USUARIO);
        } else {
            // Usuário comum não deveria acessar esta página (mas se acessar, vê apenas a si mesmo)
            usuarios = List.of(usuarioLogado);
        }
        
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("usuarioLogado", usuarioLogado);
        return "usuarios/lista";
    }

    @GetMapping("/cadastro")
    public String formularioCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Nome é obrigatório!");
            return "redirect:/usuarios/cadastro";
        }
        
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Email é obrigatório!");
            return "redirect:/usuarios/cadastro";
        }
        
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Senha é obrigatória!");
            return "redirect:/usuarios/cadastro";
        }
        
        try {
            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao cadastrar usuário: " + e.getMessage());
        }
        
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String formularioEdicao(@PathVariable Long id, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        String emailUsuarioLogado = authentication.getName();
        Optional<Usuario> usuarioLogadoOpt = usuarioService.findByEmail(emailUsuarioLogado);
        
        if (!usuarioLogadoOpt.isPresent()) {
            return "redirect:/login";
        }
        
        Usuario usuarioLogado = usuarioLogadoOpt.get();
        
        Optional<Usuario> usuarioParaEditar = usuarioService.findById(id);
        if (usuarioParaEditar.isPresent()) {
            Usuario usuario = usuarioParaEditar.get();
            
            // Verificar permissões
            if (usuarioLogado.isBibliotecario()) {
                // Bibliotecário só pode editar usuários comuns
                if (usuario.isAdmin() || usuario.isBibliotecario()) {
                    redirectAttributes.addFlashAttribute("error", "Você não tem permissão para editar este usuário!");
                    return "redirect:/usuarios";
                }
            } else if (usuarioLogado.isUsuario()) {
                // Usuário comum só pode editar a si mesmo
                if (!usuario.getId().equals(usuarioLogado.getId())) {
                    redirectAttributes.addFlashAttribute("error", "Você só pode editar seu próprio perfil!");
                    return "redirect:/usuarios";
                }
            }
            
            model.addAttribute("usuario", usuario);
            model.addAttribute("usuarioLogado", usuarioLogado);
            return "usuarios/cadastro";
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuário não encontrado!");
            return "redirect:/usuarios";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        // Verificar se bibliotecário está tentando editar admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioLogado = auth.getName();
        Optional<Usuario> usuarioLogadoOpt = usuarioService.findByEmail(emailUsuarioLogado);
        
        if (usuarioLogadoOpt.isPresent()) {
            Usuario usuarioLogado = usuarioLogadoOpt.get();
            
            Optional<Usuario> usuarioExistente = usuarioService.findById(id);
            if (usuarioExistente.isPresent()) {
                // Bibliotecário não pode editar admin
                if (usuarioLogado.isBibliotecario() && usuarioExistente.get().isAdmin()) {
                    redirectAttributes.addFlashAttribute("error", "Bibliotecário não pode editar administrador!");
                    return "redirect:/usuarios";
                }
            }
        }
        
        if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Nome é obrigatório!");
            return "redirect:/usuarios/editar/" + id;
        }
        
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Email é obrigatório!");
            return "redirect:/usuarios/editar/" + id;
        }
        
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Senha é obrigatória!");
            return "redirect:/usuarios/editar/" + id;
        }
        
        try {
            usuario.setId(id);
            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuário editado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao editar usuário: " + e.getMessage());
        }
        
        return "redirect:/usuarios";
    }

    @GetMapping("/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes, 
                                Authentication authentication) {
        String emailUsuarioLogado = authentication.getName();
        Optional<Usuario> usuarioLogadoOpt = usuarioService.findByEmail(emailUsuarioLogado);
        
        if (usuarioLogadoOpt.isPresent()) {
            Usuario usuarioLogado = usuarioLogadoOpt.get();
            
            // Verificar se bibliotecário está tentando deletar admin
            Optional<Usuario> usuarioParaDeletar = usuarioService.findById(id);
            if (usuarioParaDeletar.isPresent()) {
                if (usuarioLogado.isBibliotecario() && usuarioParaDeletar.get().isAdmin()) {
                    redirectAttributes.addFlashAttribute("error", "Bibliotecário não pode deletar administrador!");
                    return "redirect:/usuarios";
                }
            }
        }
        
        if (!usuarioService.podeDeletarUsuario(id, emailUsuarioLogado)) {
            Optional<Usuario> usuario = usuarioService.findById(id);
            if (usuario.isPresent() && usuario.get().getEmail().equals(emailUsuarioLogado)) {
                redirectAttributes.addFlashAttribute("error", "Você não pode deletar sua própria conta!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Não é possível deletar este usuário pois ele possui empréstimos ativos!");
            }
            return "redirect:/usuarios";
        }
        
        try {
            usuarioService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Usuário deletado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao deletar usuário: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/visualizar/{id}")
    public String visualizarUsuario(@PathVariable Long id, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        String emailUsuarioLogado = authentication.getName();
        Optional<Usuario> usuarioLogadoOpt = usuarioService.findByEmail(emailUsuarioLogado);
        
        if (!usuarioLogadoOpt.isPresent()) {
            return "redirect:/login";
        }
        
        Usuario usuarioLogado = usuarioLogadoOpt.get();
        
        Optional<Usuario> usuarioParaVisualizar = usuarioService.findById(id);
        if (usuarioParaVisualizar.isPresent()) {
            Usuario usuario = usuarioParaVisualizar.get();
            
            // Verificar permissões
            if (usuarioLogado.isBibliotecario()) {
                // Bibliotecário só pode visualizar usuários comuns
                if (usuario.isAdmin() || usuario.isBibliotecario()) {
                    redirectAttributes.addFlashAttribute("error", "Você não tem permissão para visualizar este usuário!");
                    return "redirect:/usuarios";
                }
            } else if (usuarioLogado.isUsuario()) {
                // Usuário comum só pode visualizar a si mesmo
                if (!usuario.getId().equals(usuarioLogado.getId())) {
                    redirectAttributes.addFlashAttribute("error", "Você só pode visualizar seu próprio perfil!");
                    return "redirect:/usuarios";
                }
            }
            
            model.addAttribute("usuario", usuario);
            model.addAttribute("usuarioLogado", usuarioLogado);
            return "usuarios/visualizar";
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuário não encontrado!");
            return "redirect:/usuarios";
        }
    }
    
    public long countAll() {
        return usuarioService.countAll();
    }
    
    public List<Usuario> findTop5UsuariosMaisAtivos() {
        return usuarioService.findAll().stream()
                .limit(5)
                .toList();
    }
}
