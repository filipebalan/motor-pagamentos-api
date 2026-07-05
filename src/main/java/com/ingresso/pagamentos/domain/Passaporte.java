package com.ingresso.pagamentos.domain;

import java.util.Objects;

public class Passaporte implements Documento {

    private final String numero;
    private final String paisEmissor;

    public Passaporte(String numero, String paisEmissor) {
        Objects.requireNonNull(numero, "O número do passaporte não pode ser nulo.");
        Objects.requireNonNull(paisEmissor, "O país emissor não pode ser nulo.");
        
        // Regra simples: Passaportes geralmente são alfanuméricos e têm pelo menos 6 caracteres
        if (numero.trim().length() < 6) {
            throw new IllegalArgumentException("Número de passaporte inválido.");
        }
        
        this.numero = numero.trim().toUpperCase();
        this.paisEmissor = paisEmissor.trim().toUpperCase();
    }

    @Override
    public String getNumero() {
        return this.numero;
    }

    @Override
    public String getTipo() {
        return "PASSAPORTE_" + this.paisEmissor;
    }
}