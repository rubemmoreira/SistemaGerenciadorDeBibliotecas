package com.biblioteca.app.config;

import com.biblioteca.app.model.Usuario;
import com.biblioteca.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("=== 🗃️ INICIANDO CARGA DE DADOS NO MYSQL ===");
        
        // Deleta e recria para garantir
        usuarioRepository.deleteByEmail("admin@biblioteca.com");
        
        Usuario admin = new Usuario();
        admin.setEmail("admin@biblioteca.com");
        admin.setSenha("123456");
        admin.setNome("Administrador");
        
        usuarioRepository.save(admin);
        System.out.println("✅ USUÁRIO ADMIN CRIADO/ATUALIZADO: admin@biblioteca.com / 123456");
        
        // Força o flush para o banco
        usuarioRepository.flush();
        
        // Verifica novamente
        var usuarioVerificado = usuarioRepository.findByEmail("admin@biblioteca.com");
        if (usuarioVerificado.isPresent()) {
            System.out.println("✅ CONFIRMAÇÃO: Usuário salvo no MySQL - " + 
                usuarioVerificado.get().getEmail() + " | " + 
                usuarioVerificado.get().getSenha());
        } else {
            System.out.println("❌ ERRO: Usuário não foi salvo no MySQL");
        }
    }
}