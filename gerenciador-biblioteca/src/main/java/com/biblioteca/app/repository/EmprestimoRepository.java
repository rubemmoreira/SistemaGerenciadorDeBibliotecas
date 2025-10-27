package com.biblioteca.app.repository;

import com.biblioteca.app.model.Emprestimo;
import com.biblioteca.app.model.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    
    List<Emprestimo> findByStatus(StatusEmprestimo status);
    
    List<Emprestimo> findByUsuarioId(Long usuarioId);
    
    List<Emprestimo> findByLivroId(Long livroId);
    
    @Query("SELECT e FROM Emprestimo e WHERE e.dataDevolucaoPrevista < :data AND e.status = 'ATIVO'")
    List<Emprestimo> findEmprestimosAtrasados(LocalDate data);
    
    Long countByStatus(StatusEmprestimo status);
}