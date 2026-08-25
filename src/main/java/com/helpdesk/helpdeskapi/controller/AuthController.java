package com.helpdesk.helpdeskapi.controller;

import com.helpdesk.helpdeskapi.dto.LoginDTO;
import com.helpdesk.helpdeskapi.dto.UsuarioRegistroDTO;
import com.helpdesk.helpdeskapi.dto.UsuarioResponseDTO;
import com.helpdesk.helpdeskapi.entity.Usuario;
import com.helpdesk.helpdeskapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Registro, login e logout de usuários")
public class AuthController {

    private static final String COOKIE_NAME = "token";

    private final AuthService authService;

    @PostMapping("/registrar")
    @Operation(summary = "Cadastra um novo usuário (comum ou técnico)")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody UsuarioRegistroDTO dto) {
        UsuarioResponseDTO usuario = authService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

   
    @PostMapping("/login")
    @Operation(summary = "Autentica o usuário e retorna o token via cookie HttpOnly")
    public ResponseEntity<UsuarioResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        AuthService.LoginResultado resultado = authService.login(dto);

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, resultado.token())
             
                .httpOnly(true)
                
                .secure(authService.isCookieSecure())
                .path("/")
                .sameSite("Lax")
                .maxAge(authService.getExpirationSeconds())
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(resultado.usuario());
    }

    @PostMapping("/logout")
    @Operation(summary = "Encerra a sessão do usuário, removendo o cookie")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(authService.isCookieSecure())
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .build();
    }

    @GetMapping("/me")
    @Operation(summary = "Retorna os dados do usuário autenticado")
    public ResponseEntity<UsuarioResponseDTO> me(@AuthenticationPrincipal Usuario usuarioLogado) {
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuarioLogado));
    }
}
