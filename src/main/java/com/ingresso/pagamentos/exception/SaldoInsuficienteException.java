package com.ingresso.pagamentos.exception;

public class SaldoInsuficienteException extends RuntimeException {
    
    public SaldoInsuficienteException(String mensagem) {
        super(mensagem);
    }
}