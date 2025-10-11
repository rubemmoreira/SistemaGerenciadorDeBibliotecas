package com.biblioteca.app.model;

import jakarta.persistence.*;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) // Garante que o email seja único
    private String email; 
    private String senha;
    private String nome;
    
    // Construtor padrão (necessário para JPA)
    public Usuario() {}

    // Getters e Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    // ... (outros getters e setters)
}