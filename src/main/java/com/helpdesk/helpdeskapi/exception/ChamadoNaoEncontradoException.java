package com.helpdesk.helpdeskapi.exception;

public class ChamadoNaoEncontradoException extends RuntimeException {

    public ChamadoNaoEncontradoException(Long id) {
        super("Chamado com id " + id + " não foi encontrado");
    }
}
