package com.helpdesk.helpdeskapi.service;

import com.helpdesk.helpdeskapi.dto.ChamadoRequestDTO;
import com.helpdesk.helpdeskapi.dto.ChamadoResponseDTO;
import com.helpdesk.helpdeskapi.entity.Chamado;
import com.helpdesk.helpdeskapi.repository.ChamadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;

   
    public ChamadoResponseDTO criar(ChamadoRequestDTO dto) {
        Chamado chamado = new Chamado();
        chamado.setTitulo(dto.titulo());
        chamado.setDescricao(dto.descricao());
        chamado.setSolicitante(dto.solicitante());
        chamado.setPrioridade(dto.prioridade());
       
        Chamado salvo = chamadoRepository.save(chamado);

        return ChamadoResponseDTO.fromEntity(salvo);
    }
}
