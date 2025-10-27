package com.biblioteca.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String senha;
    
    @Column(nullable = false)
    private String nome;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario = TipoUsuario.USUARIO;
    
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
    // Construtores
    public Usuario() {}
    
    public Usuario(String email, String senha, String nome, TipoUsuario tipoUsuario) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.tipoUsuario = tipoUsuario;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    // Métodos auxiliares
    public boolean isAdmin() {
        return TipoUsuario.ADMIN.equals(this.tipoUsuario);
    }
    
    public boolean isBibliotecario() {
        return TipoUsuario.BIBLIOTECARIO.equals(this.tipoUsuario);
    }
    
    public boolean isUsuario() {
        return TipoUsuario.USUARIO.equals(this.tipoUsuario);
    }
    
    public boolean podeGerenciarUsuarios() {
        return isAdmin() || isBibliotecario();
    }
    
    public boolean podeGerenciarLivros() {
        return isAdmin() || isBibliotecario();
    }
    
    public boolean podeGerenciarEmprestimos() {
        return isAdmin() || isBibliotecario();
    }
    
    public boolean podeVerRelatorios() {
        return isAdmin() || isBibliotecario();
    }
}