package com.biblioteca.app.repository;

import com.biblioteca.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * O Spring Data JPA gera a consulta automaticamente: SELECT * FROM usuario WHERE email = ?
     */
    Optional<Usuario> findByEmail(String email);
}