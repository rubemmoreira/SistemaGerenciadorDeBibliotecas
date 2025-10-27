package com.biblioteca.app.service;

import com.biblioteca.app.model.Usuario;
import com.biblioteca.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioService() {
        System.out.println("=== ✅ USUARIOSERVICE CONSTRUÍDO ===");
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("=== 🔍 BUSCANDO USUÁRIO: " + email + " ===");
        
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> {
                System.out.println("❌ USUÁRIO NÃO ENCONTRADO: " + email);
                return new UsernameNotFoundException("Usuário não encontrado: " + email);
            });
        
        System.out.println("✅ USUÁRIO ENCONTRADO:");
        System.out.println("   Email: " + usuario.getEmail());
        System.out.println("   Senha: " + usuario.getSenha());
        System.out.println("   Nome: " + usuario.getNome());
        
        return User.builder()
            .username(usuario.getEmail())
            .password(usuario.getSenha())
            .roles("USER")
            .build();
    }
}