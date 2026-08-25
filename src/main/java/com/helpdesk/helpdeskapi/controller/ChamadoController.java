package com.helpdesk.helpdeskapi.controller;

import com.helpdesk.helpdeskapi.dto.ChamadoRequestDTO;
import com.helpdesk.helpdeskapi.dto.ChamadoResponseDTO;
import com.helpdesk.helpdeskapi.dto.ChamadoUpdateDTO;
import com.helpdesk.helpdeskapi.entity.Usuario;
import com.helpdesk.helpdeskapi.enums.Prioridade;
import com.helpdesk.helpdeskapi.service.ChamadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ChamadoResponseDTO> criar(@Valid @RequestBody ChamadoRequestDTO dto,
        @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        ChamadoResponseDTO chamadoCriado = chamadoService.criar(dto, usuarioLogado);

        URI location = URI.create("/chamados/" + chamadoCriado.id());

        return ResponseEntity
                .created(location)
                .body(chamadoCriado);
    }

    
    @GetMapping
    @Operation(summary = "Lista todos os chamados, com filtro opcional por prioridade")
    public ResponseEntity<List<ChamadoResponseDTO>> listar(
            @RequestParam(required = false) Prioridade prioridade,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        return ResponseEntity.ok(chamadoService.listar(prioridade, usuarioLogado));
    }

    
    @GetMapping("/{id}")
    @Operation(summary = "Busca um chamado pelo id")
    public ResponseEntity<ChamadoResponseDTO> buscarPorId(@PathVariable Long id,
         @AuthenticationPrincipal Usuario usuarioLogado) {
        return ResponseEntity.ok(chamadoService.buscarPorId(id, usuarioLogado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TECNICO')")
    @Operation(summary = "Atualiza um chamado existente(Somente técnico)")
    public ResponseEntity<ChamadoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ChamadoUpdateDTO dto
    ) {
        return ResponseEntity.ok(chamadoService.atualizar(id, dto));
    }

      @PostMapping("/{id}/assumir")
    @PreAuthorize("hasRole('TECNICO')")
    @Operation(summary = "Técnico assume um chamado para si (somente técnico)")
    public ResponseEntity<ChamadoResponseDTO> assumir(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario tecnicoLogado
    ) {
        return ResponseEntity.ok(chamadoService.assumir(id, tecnicoLogado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TECNICO')")
    @Operation(summary = "Remove um chamado(Somente Tecnico)")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        chamadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
