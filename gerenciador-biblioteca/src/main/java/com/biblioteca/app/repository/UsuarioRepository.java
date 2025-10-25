package com.biblioteca.app.repository;

import com.biblioteca.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Método usando convenção de nomes do Spring Data
    Optional<Usuario> findByEmail(String email);
}