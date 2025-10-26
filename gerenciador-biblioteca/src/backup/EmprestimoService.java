package com.biblioteca.app.service;

import com.biblioteca.app.repository.EmprestimoRepository;
import com.biblioteca.app.repository.LivroRepository;
import com.biblioteca.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Comente temporariamente todo o código problemático
    /*
    public List<Emprestimo> findAll() {
        return emprestimoRepository.findAll();
    }

    // ... resto do código comentado
    */
}