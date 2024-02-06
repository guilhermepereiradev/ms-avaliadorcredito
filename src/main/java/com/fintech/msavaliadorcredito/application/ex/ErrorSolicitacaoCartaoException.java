package com.fintech.msavaliadorcredito.application.ex;

public class ErrorSolicitacaoCartaoException extends RuntimeException {
    public ErrorSolicitacaoCartaoException(String mensagem) {
        super(mensagem);
    }
}
