package com.helpdesk.helpdeskapi.service;

import com.helpdesk.helpdeskapi.dto.LoginDTO;
import com.helpdesk.helpdeskapi.dto.UsuarioRegistroDTO;
import com.helpdesk.helpdeskapi.dto.UsuarioResponseDTO;
import com.helpdesk.helpdeskapi.entity.Usuario;
import com.helpdesk.helpdeskapi.exception.EmailJaCadastradoException;
import com.helpdesk.helpdeskapi.exception.CredenciaisInvalidasException;
import com.helpdesk.helpdeskapi.repository.UsuarioRepository;
import com.helpdesk.helpdeskapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    public UsuarioResponseDTO registrar(UsuarioRegistroDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new EmailJaCadastradoException(dto.email());
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
     
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setRole(dto.role());

        Usuario salvo = usuarioRepository.save(usuario);
        return UsuarioResponseDTO.fromEntity(salvo);
    }

    public LoginResultado login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(CredenciaisInvalidasException::new);

      
        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        String token = jwtService.gerarToken(usuario.getEmail());

        return new LoginResultado(token, UsuarioResponseDTO.fromEntity(usuario));
    }

    public boolean isCookieSecure() {
        return cookieSecure;
    }

    public int getExpirationSeconds() {
        return (int) (expirationMs / 1000);
    }
    public record LoginResultado(String token, UsuarioResponseDTO usuario) {
    }
}
