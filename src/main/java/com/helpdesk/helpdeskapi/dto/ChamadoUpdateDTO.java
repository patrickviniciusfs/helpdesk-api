package com.helpdesk.helpdeskapi.dto;

import com.helpdesk.helpdeskapi.enums.Prioridade;
import com.helpdesk.helpdeskapi.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChamadoUpdateDTO(

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória")
        @Size(max = 2000, message = "A descrição deve ter no máximo 2000 caracteres")
        String descricao,

        @NotBlank(message = "O solicitante é obrigatório")
        @Size(max = 150, message = "O solicitante deve ter no máximo 150 caracteres")
        String solicitante,

        @NotNull(message = "A prioridade é obrigatória")
        Prioridade prioridade,

        @NotNull(message = "O status é obrigatório")
        Status status

) {
}
