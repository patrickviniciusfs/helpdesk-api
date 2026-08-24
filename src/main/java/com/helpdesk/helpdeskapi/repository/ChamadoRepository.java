package com.helpdesk.helpdeskapi.repository;

import com.helpdesk.helpdeskapi.entity.Chamado;
import com.helpdesk.helpdeskapi.enums.Prioridade;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    List<Chamado> findByPrioridade(Prioridade prioridade);   
}
