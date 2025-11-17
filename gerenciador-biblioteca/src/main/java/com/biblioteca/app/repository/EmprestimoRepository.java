package com.biblioteca.app.repository;

import com.biblioteca.app.model.Emprestimo;
import com.biblioteca.app.model.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    
    // Buscar empréstimos por status
    List<Emprestimo> findByStatus(StatusEmprestimo status);
    
    // Buscar empréstimos por usuário
    List<Emprestimo> findByUsuarioId(Long usuarioId);
    
    // Buscar empréstimos por livro
    List<Emprestimo> findByLivroId(Long livroId);
    
    // Validações avançadas
    long countByUsuarioIdAndStatus(Long usuarioId, StatusEmprestimo status);
    
    long countByLivroIdAndStatus(Long livroId, StatusEmprestimo status);
    
    boolean existsByLivroIdAndStatus(Long livroId, StatusEmprestimo status);
    
    boolean existsByUsuarioIdAndLivroIdAndStatus(Long usuarioId, Long livroId, StatusEmprestimo status);
    
    List<Emprestimo> findByUsuarioIdAndStatus(Long usuarioId, StatusEmprestimo status);
    
    List<Emprestimo> findByLivroIdAndStatus(Long livroId, StatusEmprestimo status);
    
    // Buscar empréstimos ativos (emprestado)
    @Query("SELECT e FROM Emprestimo e WHERE e.status = 'EMPRESTADO'")
    List<Emprestimo> findEmprestimosAtivos();
    
    // Buscar empréstimos atrasados
    @Query("SELECT e FROM Emprestimo e WHERE e.status = 'EMPRESTADO' AND e.dataDevolucaoPrevista < :dataAtual")
    List<Emprestimo> findEmprestimosAtrasados(@Param("dataAtual") LocalDateTime dataAtual);
    
    // Verificar se livro está emprestado
    @Query("SELECT COUNT(e) > 0 FROM Emprestimo e WHERE e.livro.id = :livroId AND e.status = 'EMPRESTADO'")
    boolean isLivroEmprestado(@Param("livroId") Long livroId);
    
    // Contar empréstimos por status
    long countByStatus(StatusEmprestimo status);
    
    // Buscar empréstimos por período
    @Query("SELECT e FROM Emprestimo e WHERE e.dataEmprestimo BETWEEN :dataInicio AND :dataFim")
    List<Emprestimo> findEmprestimosPorPeriodo(@Param("dataInicio") LocalDateTime dataInicio, 
                                             @Param("dataFim") LocalDateTime dataFim);
}