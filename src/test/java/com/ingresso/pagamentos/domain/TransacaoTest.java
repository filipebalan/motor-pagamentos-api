package com.ingresso.pagamentos.domain;

import com.ingresso.pagamentos.domain.valueobject.Cpf;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class TransacaoTest {

    @Test
    public void deveProcessarTransacaoComSucessoQuandoHouverSaldo() {
        // 1. Arrange (Preparar)
        Cpf cpfPagador = new Cpf("12345678909");
        Conta contaPagador = new Conta("ID-001", cpfPagador, new BigDecimal("1000.00"));

        Cpf cpfRecebedor = new Cpf("98765432100"); 
        Conta contaRecebedor = new Conta("ID-002", cpfRecebedor, new BigDecimal("0.00"));

        BigDecimal valorTransacao = new BigDecimal("500.00");
        Transacao transacao = new Transacao(contaPagador, contaRecebedor, valorTransacao);

        // 2. Act (Agir)
        transacao.processar();

        // 3. Assert (Verificar)
        assertEquals(new BigDecimal("500.00"), contaPagador.getSaldo(), "O saldo do pagador deve ser reduzido.");
        assertEquals(new BigDecimal("500.00"), contaRecebedor.getSaldo(), "O recebedor deve receber o valor.");
        assertEquals(StatusTransacao.CONCLUIDA, transacao.getStatus());
        assertNull(transacao.getMotivoFalha());
    }

    @Test
    public void deveFalharEmanterSaldosIntactosQuandoSaldoForInsuficiente() {
        // 1. Arrange (Preparar)
        Cpf cpfPagador = new Cpf("12345678909");
        Conta contaPagador = new Conta("ID-001", cpfPagador, new BigDecimal("200.00"));

        Cpf cpfRecebedor = new Cpf("98765432100");
        Conta contaRecebedor = new Conta("ID-002", cpfRecebedor, new BigDecimal("0.00"));

        BigDecimal valorTransacao = new BigDecimal("500.00");
        Transacao transacao = new Transacao(contaPagador, contaRecebedor, valorTransacao);

        // 2. Act (Agir)
        transacao.processar();

        // 3. Assert (Verificar)
        assertEquals(StatusTransacao.FALHADA, transacao.getStatus());
        assertNotNull(transacao.getMotivoFalha());
        assertEquals(new BigDecimal("200.00"), contaPagador.getSaldo(), "O saldo não deve ser alterado.");
        assertEquals(new BigDecimal("0.00"), contaRecebedor.getSaldo(), "O recebedor não deve receber o valor.");
    }
}