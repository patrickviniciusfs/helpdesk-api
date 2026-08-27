package com.helpdesk.helpdeskapi.service;

import com.helpdesk.helpdeskapi.dto.ChamadoRequestDTO;
import com.helpdesk.helpdeskapi.dto.ChamadoResponseDTO;
import com.helpdesk.helpdeskapi.dto.ChamadoUpdateDTO;
import com.helpdesk.helpdeskapi.entity.Chamado;
import com.helpdesk.helpdeskapi.entity.Usuario;
import com.helpdesk.helpdeskapi.enums.Prioridade;
import com.helpdesk.helpdeskapi.enums.Role;
import com.helpdesk.helpdeskapi.enums.Status;
import com.helpdesk.helpdeskapi.exception.AcessoNegadoException;
import com.helpdesk.helpdeskapi.exception.ChamadoNaoEncontradoException;
import com.helpdesk.helpdeskapi.repository.ChamadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;

  
    public ChamadoResponseDTO criar(ChamadoRequestDTO dto, Usuario usuarioLogado) {
        Chamado chamado = new Chamado();
        chamado.setTitulo(dto.titulo());
        chamado.setDescricao(dto.descricao());
        chamado.setPrioridade(dto.prioridade());
        chamado.setUsuario(usuarioLogado);
    

        Chamado salvo = chamadoRepository.save(chamado);

        return ChamadoResponseDTO.fromEntity(salvo);
    }

  
    public List<ChamadoResponseDTO> listar(Prioridade prioridade, Usuario usuarioLogado) {
        List<Chamado> chamados;

        boolean ehTecnico = usuarioLogado.getRole() == Role.TECNICO;

        if (ehTecnico) {
            chamados = (prioridade != null)
                    ? chamadoRepository.findByPrioridade(prioridade)
                    : chamadoRepository.findAll();
        } else {
            chamados = (prioridade != null)
                    ? chamadoRepository.findByUsuarioAndPrioridade(usuarioLogado, prioridade)
                    : chamadoRepository.findByUsuario(usuarioLogado);
        }

        return chamados.stream()
                .map(ChamadoResponseDTO::fromEntity)
                .toList();
    }

  
    public ChamadoResponseDTO buscarPorId(Long id, Usuario usuarioLogado) {
        Chamado chamado = buscarEntidadePorId(id);
        validarAcesso(chamado, usuarioLogado);
        return ChamadoResponseDTO.fromEntity(chamado);
    }

  
    public ChamadoResponseDTO atualizar(Long id, ChamadoUpdateDTO dto) {
        Chamado chamado = buscarEntidadePorId(id);

        chamado.setTitulo(dto.titulo());
        chamado.setDescricao(dto.descricao());
        chamado.setPrioridade(dto.prioridade());
        chamado.setStatus(dto.status());
     

        Chamado atualizado = chamadoRepository.save(chamado);

        return ChamadoResponseDTO.fromEntity(atualizado);
    }


    public ChamadoResponseDTO assumir(Long id, Usuario tecnicoLogado) {
        Chamado chamado = buscarEntidadePorId(id);

        chamado.setTecnicoResponsavel(tecnicoLogado);
        if (chamado.getStatus() == Status.ABERTO) {
            chamado.setStatus(Status.EM_ANDAMENTO);
        }

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

  
    private void validarAcesso(Chamado chamado, Usuario usuarioLogado) {
        boolean ehTecnico = usuarioLogado.getRole() == Role.TECNICO;
        boolean ehDono = chamado.getUsuario().getId().equals(usuarioLogado.getId());

        if (!ehTecnico && !ehDono) {
            throw new AcessoNegadoException("Você não tem permissão para acessar este chamado");
        }
    }
}
