package com.biblioteca.app.repository;

import com.biblioteca.app.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    
    // Busca livros por título (ignora maiúsculas/minúsculas)
    List<Livro> findByTituloContainingIgnoreCase(String titulo);
    
    // Busca livros por autor (ignora maiúsculas/minúsculas)
    List<Livro> findByAutorContainingIgnoreCase(String autor);
    
    // Busca livros por categoria (ignora maiúsculas/minúsculas)
    List<Livro> findByCategoriaContainingIgnoreCase(String categoria);
    
    // Validações avançadas
    boolean existsByIsbn(String isbn);
    
    boolean existsByIsbnAndIdNot(String isbn, Long id);
    
    List<Livro> findByQuantidadeGreaterThan(int quantidade);
    
    List<Livro> findByQuantidadeLessThanEqual(int quantidade);
    
    long countByCategoria(String categoria);
    
    // Busca geral por título, autor ou categoria
    @Query("SELECT l FROM Livro l WHERE " +
           "LOWER(l.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(l.autor) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(l.categoria) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Livro> findByTermoBusca(@Param("termo") String termo);
}