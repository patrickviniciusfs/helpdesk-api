package com.helpdesk.helpdeskapi.dto;

import com.helpdesk.helpdeskapi.entity.Chamado;
import com.helpdesk.helpdeskapi.enums.Prioridade;
import com.helpdesk.helpdeskapi.enums.Status;

import java.time.LocalDateTime;


public record ChamadoResponseDTO(
        Long id,
        String titulo,
        String descricao,
        String solicitante,
        Prioridade prioridade,
        Status status,
        LocalDateTime dataCriacao
) {


    public static ChamadoResponseDTO fromEntity(Chamado chamado) {
        return new ChamadoResponseDTO(
                chamado.getId(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                chamado.getSolicitante(),
                chamado.getPrioridade(),
                chamado.getStatus(),
                chamado.getDataCriacao()
        );
    }
}
