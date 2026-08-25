package com.helpdesk.helpdeskapi.dto;

import com.helpdesk.helpdeskapi.entity.Usuario;

public record UsuarioResumoDTO(
        Long id,
        String nome,
        String email
) {
    public static UsuarioResumoDTO fromEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioResumoDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
