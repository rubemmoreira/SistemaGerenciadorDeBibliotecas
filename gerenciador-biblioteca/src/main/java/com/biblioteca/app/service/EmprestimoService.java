package com.biblioteca.app.service;

import com.biblioteca.app.model.Emprestimo;
import com.biblioteca.app.model.Livro;
import com.biblioteca.app.model.StatusEmprestimo;
import com.biblioteca.app.model.Usuario;
import com.biblioteca.app.repository.EmprestimoRepository;
import com.biblioteca.app.repository.LivroRepository;
import com.biblioteca.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmprestimoService {
    
    @Autowired
    private EmprestimoRepository emprestimoRepository;
    
    @Autowired
    private LivroRepository livroRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    // Métodos CRUD básicos
    public List<Emprestimo> findAll() {
        return emprestimoRepository.findAll();
    }

    public Optional<Emprestimo> findById(Long id) {
        return emprestimoRepository.findById(id);
    }

    public Emprestimo save(Emprestimo emprestimo) {
        return emprestimoRepository.save(emprestimo);
    }

    public void deleteById(Long id) {
        emprestimoRepository.deleteById(id);
    }
    
    // Métodos específicos de empréstimo
    public Emprestimo realizarEmprestimo(Long livroId, Long usuarioId) {
        Livro livro = livroRepository.findById(livroId)
            .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // Verificar se o livro está disponível
        if (livro.getQuantidade() <= 0) {
            throw new RuntimeException("Livro não disponível para empréstimo");
        }
        
        // Verificar se o usuário já tem este livro emprestado
        if (emprestimoRepository.existsByUsuarioIdAndLivroIdAndStatus(usuarioId, livroId, StatusEmprestimo.EMPRESTADO)) {
            throw new RuntimeException("Usuário já possui este livro emprestado");
        }
        
        // Criar empréstimo
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setUsuario(usuario);
        emprestimo.setDataEmprestimo(LocalDateTime.now());
        emprestimo.setDataDevolucaoPrevista(LocalDateTime.now().plusDays(7)); // 7 dias para devolução
        emprestimo.setStatus(StatusEmprestimo.EMPRESTADO);
        
        // Reduzir quantidade do livro
        livro.setQuantidade(livro.getQuantidade() - 1);
        livroRepository.save(livro);
        
        return emprestimoRepository.save(emprestimo);
    }
    
    public Emprestimo devolverLivro(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
            .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));
        
        if (emprestimo.getStatus() != StatusEmprestimo.EMPRESTADO) {
            throw new RuntimeException("Empréstimo já foi devolvido");
        }
        
        // Atualizar status
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
        emprestimo.setDataDevolucaoReal(LocalDateTime.now());
        
        // Aumentar quantidade do livro
        Livro livro = emprestimo.getLivro();
        livro.setQuantidade(livro.getQuantidade() + 1);
        livroRepository.save(livro);
        
        return emprestimoRepository.save(emprestimo);
    }
    
    // Métodos para relatórios
    public long countAll() {
        return emprestimoRepository.count();
    }
    
    public long countEmprestimosAtivos() {
        return emprestimoRepository.countByStatus(StatusEmprestimo.EMPRESTADO);
    }
    
    public long countEmprestimosDevolvidos() {
        return emprestimoRepository.countByStatus(StatusEmprestimo.DEVOLVIDO);
    }
    
    public long countEmprestimosAtrasados() {
        return emprestimoRepository.countByStatus(StatusEmprestimo.ATRASADO);
    }
    
    public List<Emprestimo> findEmprestimosAtivos() {
        return emprestimoRepository.findEmprestimosAtivos();
    }
    
    public List<Emprestimo> findEmprestimosAtrasados() {
        return emprestimoRepository.findEmprestimosAtrasados(LocalDateTime.now());
    }
    
    // Buscar empréstimos por usuário
    public List<Emprestimo> findByUsuarioId(Long usuarioId) {
        return emprestimoRepository.findByUsuarioId(usuarioId);
    }
}