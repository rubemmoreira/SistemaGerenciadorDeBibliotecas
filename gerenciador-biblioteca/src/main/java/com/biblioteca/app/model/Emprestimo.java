package com.biblioteca.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emprestimo")
public class Emprestimo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(name = "data_emprestimo", nullable = false)
    private LocalDateTime dataEmprestimo;
    
    @Column(name = "data_devolucao_prevista", nullable = false)
    private LocalDateTime dataDevolucaoPrevista;
    
    @Column(name = "data_devolucao_real")
    private LocalDateTime dataDevolucaoReal;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEmprestimo status;
    
    @Column(name = "observacoes", length = 500)
    private String observacoes;
    
    // Construtores
    public Emprestimo() {}
    
    public Emprestimo(Livro livro, Usuario usuario, LocalDateTime dataEmprestimo, 
                     LocalDateTime dataDevolucaoPrevista, StatusEmprestimo status) {
        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.status = status;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Livro getLivro() {
        return livro;
    }
    
    public void setLivro(Livro livro) {
        this.livro = livro;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public LocalDateTime getDataEmprestimo() {
        return dataEmprestimo;
    }
    
    public void setDataEmprestimo(LocalDateTime dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }
    
    public LocalDateTime getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }
    
    public void setDataDevolucaoPrevista(LocalDateTime dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }
    
    public LocalDateTime getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }
    
    public void setDataDevolucaoReal(LocalDateTime dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }
    
    public StatusEmprestimo getStatus() {
        return status;
    }
    
    public void setStatus(StatusEmprestimo status) {
        this.status = status;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    // Métodos auxiliares
    public boolean isAtrasado() {
        return status == StatusEmprestimo.EMPRESTADO && 
               LocalDateTime.now().isAfter(dataDevolucaoPrevista);
    }
    
    public boolean isDevolvido() {
        return status == StatusEmprestimo.DEVOLVIDO;
    }
    
    public boolean isEmprestado() {
        return status == StatusEmprestimo.EMPRESTADO;
    }
}
