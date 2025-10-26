package com.biblioteca.app.repository;

import com.biblioteca.app.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    // Apenas os métodos básicos do JpaRepository por enquanto
    // Adicionaremos métodos customizados depois que compilar
}