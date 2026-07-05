package com.ingresso.pagamentos.domain;

import com.ingresso.pagamentos.exception.SaldoInsuficienteException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Embedded;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "contas") // Define o nome da tabela na base de dados
public class Conta {
    
    @Id
    private String idConta;

    @Embedded // O número do CPF ficará na mesma tabela da conta
    private Cpf titular;
    
    private BigDecimal saldo;

    // Construtor obrigatório para o JPA
    protected Conta() {}

    public Conta(String idConta, Cpf titular, BigDecimal saldoInicial) {
        Objects.requireNonNull(idConta, "O ID da conta não pode ser nulo.");
        Objects.requireNonNull(titular, "Toda conta deve pertencer a um titular.");
        if (saldoInicial == null || saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        
        this.idConta = idConta;
        this.titular = titular;
        this.saldo = saldoInicial;
    }

    public void debitar(BigDecimal valorParaDebito) {
        if (valorParaDebito == null || valorParaDebito.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor de débito deve ser maior que zero.");
        }
        if (this.saldo.compareTo(valorParaDebito) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente.");
        }
        this.saldo = this.saldo.subtract(valorParaDebito);
    }

    public void creditar(BigDecimal valorParaCredito) {
        if (valorParaCredito == null || valorParaCredito.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor de crédito deve ser maior que zero.");
        }
        this.saldo = this.saldo.add(valorParaCredito);
    }

    public String getIdConta() { return idConta; }
    public Documento getTitular() { return titular; }
    public BigDecimal getSaldo() { return saldo; }
}