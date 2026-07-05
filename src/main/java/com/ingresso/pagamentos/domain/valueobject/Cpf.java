package com.ingresso.pagamentos.domain.valueobject;

import java.util.Objects;

public class Cpf {

    // 1. O valor é imutável (final). Um VO nunca muda de estado.
    private final String numero;

    // 2. A validação acontece na instância (Construtor)
    public Cpf(String numero) {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("O CPF não pode ser nulo ou vazio.");
        }
        
        String cpfLimpo = numero.replaceAll("\\D", ""); // Remove pontos e traços
        
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("O CPF deve conter exatamente 11 dígitos.");
        }
        
        // (Aqui entraria aquele cálculo matemático padrão de CPF do módulo 11)

        this.numero = cpfLimpo;
    }

    public String getNumero() {
        return numero;
    }

    // 3. Em DDD, Value Objects são comparados pelo seu valor, e não pelo seu ID na memória.
    // Por isso, equals() e hashCode() são obrigatórios.
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