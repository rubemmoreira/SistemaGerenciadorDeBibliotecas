package com.biblioteca.app.service;

import com.biblioteca.app.model.Livro;
import com.biblioteca.app.repository.LivroRepository;
import com.biblioteca.app.repository.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;
    
    @Autowired
    private EmprestimoRepository emprestimoRepository;

    public List<Livro> findAll() {
        return livroRepository.findAll();
    }

    public Optional<Livro> findById(Long id) {
        return livroRepository.findById(id);
    }

    public Livro save(Livro livro) {
        return livroRepository.save(livro);
    }

    public void deleteById(Long id) {
        livroRepository.deleteById(id);
    }
    
    public boolean podeDeletarLivro(Long livroId) {
        if (!livroRepository.existsById(livroId)) {
            return false;
        }
        
        return !emprestimoRepository.existsByLivroIdAndStatus(livroId, com.biblioteca.app.model.StatusEmprestimo.EMPRESTADO);
    }
    
    public boolean isbnExiste(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        return livroRepository.existsByIsbn(isbn.trim());
    }
    
    public boolean isbnExisteParaOutroLivro(String isbn, Long id) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        return livroRepository.existsByIsbnAndIdNot(isbn.trim(), id);
    }
    
    public boolean validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return false;
        }
        return titulo.trim().length() >= 2;
    }
    
    public boolean validarAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            return false;
        }
        return autor.trim().length() >= 2;
    }
    
    public boolean validarQuantidade(Integer quantidade) {
        if (quantidade == null) {
            return false;
        }
        return quantidade >= 0;
    }
    
    public boolean validarAnoPublicacao(Integer ano) {
        if (ano == null) {
            return false;
        }
        int anoAtual = java.time.Year.now().getValue();
        return ano >= 1000 && ano <= anoAtual + 1; // Permite até 1 ano no futuro
    }
    
    public boolean validarIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return true; // ISBN é opcional
        }
        // Remove hífens e espaços
        String isbnLimpo = isbn.replaceAll("[\\s-]", "");
        // Verifica se tem 10 ou 13 dígitos
        return isbnLimpo.matches("\\d{10}|\\d{13}");
    }
    
    public List<Livro> findLivrosComEstoqueBaixo(int limiteMinimo) {
        return livroRepository.findByQuantidadeLessThanEqual(limiteMinimo);
    }
    
    public List<Livro> findLivrosComEstoque() {
        return livroRepository.findByQuantidadeGreaterThan(0);
    }
    
    // Métodos de busca
    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }
    
    public List<Livro> buscarPorAutor(String autor) {
        return livroRepository.findByAutorContainingIgnoreCase(autor);
    }
    
    public List<Livro> buscarPorCategoria(String categoria) {
        return livroRepository.findByCategoriaContainingIgnoreCase(categoria);
    }
    
    public List<Livro> buscarPorTermo(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return findAll();
        }
        return livroRepository.findByTermoBusca(termo.trim());
    }
    
    // Método para relatórios
    public long countAll() {
        return livroRepository.count();
    }
    
    public List<Livro> findTop5LivrosMaisEmprestados() {
        // Por enquanto retorna os primeiros 5 livros
        // Em um sistema real, isso seria calculado baseado nos empréstimos
        return livroRepository.findAll().stream()
                .limit(5)
                .toList();
    }
}
