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
import java.time.format.DateTimeFormatter;
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
        return realizarEmprestimo(livroId, usuarioId, 7);
    }
    
    public Emprestimo realizarEmprestimo(Long livroId, Long usuarioId, Integer diasEmprestimo) {
        Livro livro = livroRepository.findById(livroId)
            .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // Verificar se o livro está disponível
        if (livro.getQuantidade() == null || livro.getQuantidade() <= 0) {
            throw new RuntimeException("Livro não disponível para empréstimo");
        }
        
        // Contar quantos empréstimos ativos existem para este livro
        long emprestimosAtivos = emprestimoRepository.countByLivroIdAndStatus(livroId, StatusEmprestimo.EMPRESTADO);
        
        // Verificar se há livros disponíveis (quantidade total - empréstimos ativos)
        long livrosDisponiveis = livro.getQuantidade() - emprestimosAtivos;
        
        if (livrosDisponiveis <= 0) {
            throw new RuntimeException("Todos os exemplares deste livro já estão emprestados");
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
        emprestimo.setDataDevolucaoPrevista(LocalDateTime.now().plusDays(diasEmprestimo));
        emprestimo.setStatus(StatusEmprestimo.EMPRESTADO);
        
        // Não precisamos mais reduzir a quantidade do livro manualmente,
        // pois agora controlamos pela contagem de empréstimos ativos
        // Mas mantemos para manter a consistência dos dados
        // livro.setQuantidade(livro.getQuantidade() - 1);
        // livroRepository.save(livro);
        
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
    
    public Emprestimo atualizarEmprestimo(Long emprestimoId, Long livroId, Long usuarioId, 
                                          String status, String dataEmprestimo, 
                                          String dataDevolucaoPrevista, 
                                          String dataDevolucaoReal, String observacoes) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
            .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));
        
        Livro livro = livroRepository.findById(livroId)
            .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        StatusEmprestimo novoStatus = StatusEmprestimo.valueOf(status);
        StatusEmprestimo statusAnterior = emprestimo.getStatus();
        Long livroAnteriorId = emprestimo.getLivro().getId();
        boolean mudouLivro = !livroAnteriorId.equals(livroId);
        boolean mudouParaEmprestado = (statusAnterior != StatusEmprestimo.EMPRESTADO) && (novoStatus == StatusEmprestimo.EMPRESTADO);
        
        // Se está mudando para EMPRESTADO ou mudou o livro, verificar disponibilidade
        if (mudouParaEmprestado || (mudouLivro && novoStatus == StatusEmprestimo.EMPRESTADO)) {
            // Contar quantos empréstimos ativos existem para este livro
            long emprestimosAtivos = emprestimoRepository.countByLivroIdAndStatus(livroId, StatusEmprestimo.EMPRESTADO);
            
            // Se o empréstimo atual já estava EMPRESTADO e não mudou o livro,
            // ele já está contado na contagem, então precisamos subtrair 1
            if (statusAnterior == StatusEmprestimo.EMPRESTADO && !mudouLivro) {
                emprestimosAtivos--; // Não contar o empréstimo atual que já estava ativo
            }
            
            // Verificar se há livros disponíveis
            long livrosDisponiveis = livro.getQuantidade() - emprestimosAtivos;
            
            if (livrosDisponiveis <= 0) {
                throw new RuntimeException("Todos os exemplares deste livro já estão emprestados");
            }
            
            // Verificar se o usuário já tem este livro emprestado (exceto o empréstimo atual)
            if (emprestimoRepository.existsByUsuarioIdAndLivroIdAndStatus(usuarioId, livroId, StatusEmprestimo.EMPRESTADO)) {
                // Se não mudou o livro e o usuário, está ok (é o mesmo empréstimo)
                if (mudouLivro || !emprestimo.getUsuario().getId().equals(usuarioId)) {
                    throw new RuntimeException("Usuário já possui este livro emprestado");
                }
            }
        }
        
        // Atualizar dados do empréstimo
        emprestimo.setLivro(livro);
        emprestimo.setUsuario(usuario);
        emprestimo.setStatus(novoStatus);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        emprestimo.setDataEmprestimo(LocalDateTime.parse(dataEmprestimo, formatter));
        emprestimo.setDataDevolucaoPrevista(LocalDateTime.parse(dataDevolucaoPrevista, formatter));
        
        if (dataDevolucaoReal != null && !dataDevolucaoReal.isEmpty()) {
            emprestimo.setDataDevolucaoReal(LocalDateTime.parse(dataDevolucaoReal, formatter));
        }
        
        emprestimo.setObservacoes(observacoes);
        
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