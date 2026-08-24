package com.helpdesk.helpdeskapi.controller;

import com.helpdesk.helpdeskapi.dto.ChamadoRequestDTO;
import com.helpdesk.helpdeskapi.dto.ChamadoResponseDTO;
import com.helpdesk.helpdeskapi.dto.ChamadoUpdateDTO;
import com.helpdesk.helpdeskapi.enums.Prioridade;
import com.helpdesk.helpdeskapi.service.ChamadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;


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

    
    @GetMapping
    @Operation(summary = "Lista todos os chamados, com filtro opcional por prioridade")
    public ResponseEntity<List<ChamadoResponseDTO>> listar(
            @RequestParam(required = false) Prioridade prioridade
    ) {
        return ResponseEntity.ok(chamadoService.listar(prioridade));
    }

    
    @GetMapping("/{id}")
    @Operation(summary = "Busca um chamado pelo id")
    public ResponseEntity<ChamadoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(chamadoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um chamado existente")
    public ResponseEntity<ChamadoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ChamadoUpdateDTO dto
    ) {
        return ResponseEntity.ok(chamadoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um chamado")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        chamadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
