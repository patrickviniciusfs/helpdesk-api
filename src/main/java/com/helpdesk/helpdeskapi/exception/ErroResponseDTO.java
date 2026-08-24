package com.helpdesk.helpdeskapi.exception;

import java.time.LocalDateTime;
import java.util.List;


public record ErroResponseDTO(
        LocalDateTime timestamp,
        int status,
        String erro,
        List<String> mensagens
) {
    public static ErroResponseDTO of(int status, String erro, List<String> mensagens) {
        return new ErroResponseDTO(LocalDateTime.now(), status, erro, mensagens);
    }
}
