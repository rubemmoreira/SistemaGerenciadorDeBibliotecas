package com.biblioteca.app.service;

import com.biblioteca.app.model.Usuario;
import com.biblioteca.app.model.TipoUsuario;
import com.biblioteca.app.repository.UsuarioRepository;
import com.biblioteca.app.repository.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EmprestimoRepository emprestimoRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
        
        if (!usuario.getAtivo()) {
            throw new UsernameNotFoundException("Usuário inativo: " + email);
        }
        
        return User.builder()
            .username(usuario.getEmail())
            .password(usuario.getSenha())
            .roles(usuario.getTipoUsuario().name())
            .build();
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> findAtivos() {
        return usuarioRepository.findByAtivoTrue();
    }
    
    public List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario) {
        return usuarioRepository.findByTipoUsuario(tipoUsuario);
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
    
    public void deleteByIdAndInvalidateSession(Long id, String emailUsuarioLogado) {
        usuarioRepository.deleteById(id);
        
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent() && usuario.get().getEmail().equals(emailUsuarioLogado)) {
            System.out.println("⚠️ Usuário deletou a si mesmo - sessão deve ser invalidada");
        }
    }
    
    // Validações avançadas
    public boolean podeDeletarUsuario(Long usuarioId, String emailUsuarioLogado) {
        if (!usuarioRepository.existsById(usuarioId)) {
            return false;
        }
        
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent() && usuario.get().getEmail().equals(emailUsuarioLogado)) {
            return false;
        }
        
        return emprestimoRepository.countByUsuarioIdAndStatus(usuarioId, com.biblioteca.app.model.StatusEmprestimo.EMPRESTADO) == 0;
    }
    
    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    
    public boolean emailExisteParaOutroUsuario(String email, Long id) {
        return usuarioRepository.existsByEmailAndIdNot(email, id);
    }
    
    public boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    public boolean validarSenha(String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            return false;
        }
        return senha.length() >= 6;
    }
    
    public boolean validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        return nome.trim().length() >= 2;
    }
    
    public long countAll() {
        return usuarioRepository.count();
    }
    
    public List<Usuario> findTop5UsuariosMaisAtivos() {
        return usuarioRepository.findAll().stream()
                .limit(5)
                .toList();
    }
    
    public boolean podeGerenciarUsuarios(Usuario usuario) {
        return usuario.podeGerenciarUsuarios();
    }
    
    public boolean podeGerenciarLivros(Usuario usuario) {
        return usuario.podeGerenciarLivros();
    }
    
    public boolean podeGerenciarEmprestimos(Usuario usuario) {
        return usuario.podeGerenciarEmprestimos();
    }
    
    public boolean podeVerRelatorios(Usuario usuario) {
        return usuario.podeVerRelatorios();
    }
}