package com.helpdesk.helpdeskapi.dto;

import com.helpdesk.helpdeskapi.entity.Chamado;
import com.helpdesk.helpdeskapi.enums.Prioridade;
import com.helpdesk.helpdeskapi.enums.Status;

import java.time.LocalDateTime;


public record ChamadoResponseDTO(
         Long id,
        String titulo,
        String descricao,
        UsuarioResumoDTO solicitante,
        UsuarioResumoDTO tecnicoResponsavel,
        Prioridade prioridade,
        Status status,
        LocalDateTime dataCriacao
) {


    public static ChamadoResponseDTO fromEntity(Chamado chamado) {
        return new ChamadoResponseDTO(
                  chamado.getId(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                UsuarioResumoDTO.fromEntity(chamado.getUsuario()),
                UsuarioResumoDTO.fromEntity(chamado.getTecnicoResponsavel()),
                chamado.getPrioridade(),
                chamado.getStatus(),
                chamado.getDataCriacao()
        );
    }
}
