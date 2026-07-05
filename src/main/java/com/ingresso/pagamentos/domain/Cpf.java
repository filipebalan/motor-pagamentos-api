package com.ingresso.pagamentos.domain;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable // Indica que esta classe será embutida na tabela da Conta
public class Cpf implements Documento {
    
    private String numero;

    // Construtor obrigatório para o JPA
    protected Cpf() {}

    public Cpf(String numero) {
        Objects.requireNonNull(numero, "O CPF não pode ser nulo.");
        String cpfLimpo = numero.replaceAll("[^0-9]", "");
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        this.numero = cpfLimpo;
    }

    @Override
    public String getNumero() { return this.numero; }

    @Override
    public String getTipo() { return "CPF"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cpf cpf = (Cpf) o;
        return numero.equals(cpf.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}