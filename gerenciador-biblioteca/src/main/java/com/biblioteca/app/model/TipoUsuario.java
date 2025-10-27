package com.biblioteca.app.model;

public enum TipoUsuario {
    ADMIN("Administrador"),
    BIBLIOTECARIO("Bibliotecário"),
    USUARIO("Usuário");
    
    private final String descricao;
    
    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    @Override
    public String toString() {
        return descricao;
    }
}
