package com.biblioteca.repository;

import com.biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    Optional<Livro> findByIsbn(String isbn);
    List<Livro> findByAutorContainingIgnoreCase(String autor);
    List<Livro> findByTituloContainingIgnoreCase(String titulo);
    List<Livro> findByDisponivelTrue();
}