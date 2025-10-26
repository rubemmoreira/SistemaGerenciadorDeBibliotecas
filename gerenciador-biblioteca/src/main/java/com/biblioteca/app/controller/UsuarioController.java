package com.biblioteca.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    public UsuarioController() {
        System.out.println("=== ✅ USUARIOCONTROLLER CONSTRUÍDO ===");
    }

    @GetMapping({"", "/"})
    public String listarUsuarios(Model model) {
        System.out.println("=== 👥 ACESSANDO PÁGINA DE USUÁRIOS ===");
        System.out.println("📁 Tentando carregar: usuarios/lista.html");
        return "usuarios/lista";
    }

    @GetMapping("/teste")
    public String testeUsuario() {
        System.out.println("=== 🧪 TESTE USUARIOS ACESSADO ===");
        return "usuarios/lista";
    }
}