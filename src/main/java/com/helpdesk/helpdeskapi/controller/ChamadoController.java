package com.helpdesk.helpdeskapi.controller;

import com.helpdesk.helpdeskapi.dto.ChamadoRequestDTO;
import com.helpdesk.helpdeskapi.dto.ChamadoResponseDTO;
import com.helpdesk.helpdeskapi.service.ChamadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
@RequestMapping("/chamados")
@RequiredArgsConstructor
@Tag(name = "Chamados", description = "Gerenciamento de chamados técnicos")
public class ChamadoController {

    private final ChamadoService chamadoService;

   
    @PostMapping
    @Operation(summary = "Cria um novo chamado técnico")
    public ResponseEntity<ChamadoResponseDTO> criar(@Valid @RequestBody ChamadoRequestDTO dto) {
        ChamadoResponseDTO chamadoCriado = chamadoService.criar(dto);

        URI location = URI.create("/chamados/" + chamadoCriado.id());

        return ResponseEntity
                .created(location)
                .body(chamadoCriado);
    }
}
