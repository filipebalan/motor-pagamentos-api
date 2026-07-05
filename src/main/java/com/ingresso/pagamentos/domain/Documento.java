package com.ingresso.pagamentos.domain;

public interface Documento {
    String getNumero();
    String getTipo(); // Ex: "CPF", "PASSAPORTE"
}