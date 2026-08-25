package com.helpdesk.helpdeskapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChamadoNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> handleChamadoNaoEncontrado(ChamadoNaoEncontradoException ex) {
        ErroResponseDTO erro = ErroResponseDTO.of(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErroResponseDTO> handleEmailJaCadastrado(EmailJaCadastradoException ex) {
        ErroResponseDTO erro = ErroResponseDTO.of(
                HttpStatus.CONFLICT.value(),
                "Conflito de dados",
                List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErroResponseDTO> handleCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        ErroResponseDTO erro = ErroResponseDTO.of(
                HttpStatus.UNAUTHORIZED.value(),
                "Falha na autenticação",
                List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ErroResponseDTO> handleAcessoNegado(AcessoNegadoException ex) {
        ErroResponseDTO erro = ErroResponseDTO.of(
                HttpStatus.FORBIDDEN.value(),
                "Acesso negado",
                List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErroResponseDTO> handleSpringAccessDenied(
            org.springframework.security.access.AccessDeniedException ex) {
        ErroResponseDTO erro = ErroResponseDTO.of(
                HttpStatus.FORBIDDEN.value(),
                "Acesso negado",
                List.of("Você não tem permissão para executar esta ação"));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handleValidacao(MethodArgumentNotValidException ex) {
        List<String> mensagens = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();

        ErroResponseDTO erro = ErroResponseDTO.of(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                mensagens);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> handleGenerico(Exception ex) {
        ErroResponseDTO erro = ErroResponseDTO.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno no servidor",
                List.of("Ocorreu um erro inesperado. Tente novamente mais tarde."));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}
