package com.helpdesk.helpdeskapi.dto;

import com.helpdesk.helpdeskapi.entity.Usuario;
import com.helpdesk.helpdeskapi.enums.Role;


public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        Role role
) {
    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole()
        );
    }
}
