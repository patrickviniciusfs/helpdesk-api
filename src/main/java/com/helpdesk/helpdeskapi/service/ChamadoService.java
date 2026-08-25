package com.helpdesk.helpdeskapi.service;

import com.helpdesk.helpdeskapi.dto.ChamadoRequestDTO;
import com.helpdesk.helpdeskapi.dto.ChamadoResponseDTO;
import com.helpdesk.helpdeskapi.dto.ChamadoUpdateDTO;
import com.helpdesk.helpdeskapi.entity.Chamado;
import com.helpdesk.helpdeskapi.entity.Usuario;
import com.helpdesk.helpdeskapi.enums.Prioridade;
import com.helpdesk.helpdeskapi.exception.ChamadoNaoEncontradoException;
import com.helpdesk.helpdeskapi.repository.ChamadoRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;

   
    public ChamadoResponseDTO criar(ChamadoRequestDTO dto, Usuario usuario) {
        Chamado chamado = new Chamado();
        chamado.setTitulo(dto.titulo());
        chamado.setDescricao(dto.descricao());
        chamado.setSolicitante(dto.solicitante());
        chamado.setPrioridade(dto.prioridade());
        chamado.setUsuario(usuario);
       
        Chamado salvo = chamadoRepository.save(chamado);

        return ChamadoResponseDTO.fromEntity(salvo);
    }

    public List<ChamadoResponseDTO> listar(Prioridade prioridade, Usuario usuario) {
        List<Chamado> chamados = (prioridade != null)
                ? chamadoRepository.findByPrioridade(prioridade)
                : chamadoRepository.findAll();

        return chamados.stream()
                .map(ChamadoResponseDTO::fromEntity)
                .toList();
    }

    public ChamadoResponseDTO buscarPorId(Long id, Usuario usuario) {
        Chamado chamado = buscarEntidadePorId(id);
        return ChamadoResponseDTO.fromEntity(chamado);
    }

    public ChamadoResponseDTO assumir(Long id, Usuario tecnico) {
        Chamado chamado = buscarEntidadePorId(id);
        chamado.setUsuario(tecnico);
        Chamado atualizado = chamadoRepository.save(chamado);
        return ChamadoResponseDTO.fromEntity(atualizado);
    }

    
    public ChamadoResponseDTO atualizar(Long id, ChamadoUpdateDTO dto) {
        Chamado chamado = buscarEntidadePorId(id);

        chamado.setTitulo(dto.titulo());
        chamado.setDescricao(dto.descricao());
        chamado.setSolicitante(dto.solicitante());
        chamado.setPrioridade(dto.prioridade());
        chamado.setStatus(dto.status());
        
        Chamado atualizado = chamadoRepository.save(chamado);

        return ChamadoResponseDTO.fromEntity(atualizado);
    }

    
    public void deletar(Long id) {
        Chamado chamado = buscarEntidadePorId(id);
        chamadoRepository.delete(chamado);
    }

    private Chamado buscarEntidadePorId(Long id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new ChamadoNaoEncontradoException(id));
    }
}

