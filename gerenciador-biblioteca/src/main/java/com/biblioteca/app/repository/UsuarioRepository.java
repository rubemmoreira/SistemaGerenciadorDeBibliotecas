package com.biblioteca.app.repository;

import com.biblioteca.app.model.Usuario;
import com.biblioteca.app.model.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    
    // Validações avançadas
    boolean existsByEmail(String email);
    
    boolean existsByEmailAndIdNot(String email, Long id);
    
    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);
    
    List<Usuario> findByAtivoTrue();
    
    long countByTipoUsuario(TipoUsuario tipoUsuario);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Usuario u WHERE u.email = :email")
    void deleteByEmail(@Param("email") String email);
    
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.ativo = :ativo WHERE u.id = :id")
    void updateAtivoStatus(@Param("id") Long id, @Param("ativo") Boolean ativo);
}
