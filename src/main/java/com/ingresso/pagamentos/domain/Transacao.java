package com.ingresso.pagamentos.domain;

import com.ingresso.pagamentos.exception.SaldoInsuficienteException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transacao {

    private final String idTransacao;
    private final Conta contaOrigem;
    private final Conta contaDestino;
    private final BigDecimal valor;
    private final LocalDateTime dataHoraCriacao;
    
    private StatusTransacao status;
    private String motivoFalha;

    public Transacao(Conta contaOrigem, Conta contaDestino, BigDecimal valor) {
        if (contaOrigem == null || contaDestino == null) {
            throw new IllegalArgumentException("As contas de origem e destino são obrigatórias.");
        }
        if (contaOrigem.getIdConta().equals(contaDestino.getIdConta())) {
            throw new IllegalArgumentException("A conta de origem não pode ser igual à de destino.");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser estritamente positivo.");
        }

        this.idTransacao = UUID.randomUUID().toString();
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.valor = valor;
        this.dataHoraCriacao = LocalDateTime.now();
        this.status = StatusTransacao.PENDENTE;
    }

    public void processar() {
        if (this.status != StatusTransacao.PENDENTE) {
            throw new IllegalStateException("Apenas transações pendentes podem ser processadas.");
        }

        try {
            this.contaOrigem.debitar(this.valor);
            this.contaDestino.creditar(this.valor);
            this.status = StatusTransacao.CONCLUIDA;
            
        } catch (SaldoInsuficienteException e) {
            this.status = StatusTransacao.FALHADA;
            this.motivoFalha = e.getMessage();
        } catch (Exception e) {
            this.status = StatusTransacao.FALHADA;
            this.motivoFalha = "Erro interno ao processar a transação.";
        }
    }

    public String getIdTransacao() { return idTransacao; }
    public LocalDateTime getDataHoraCriacao() { return dataHoraCriacao; }
    public StatusTransacao getStatus() { return status; }
    public String getMotivoFalha() { return motivoFalha; }
}