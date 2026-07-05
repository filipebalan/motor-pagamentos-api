package com.ingresso.pagamentos.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class TransacaoTest {

    @Test
    public void deveProcessarTransacaoComSucessoQuandoHouverSaldo() {
        // 1. Arrange (Preparar)
        Cpf cpfFilipe = new Cpf("12345678909");
        Conta contaFilipe = new Conta("ID-001", cpfFilipe, new BigDecimal("1000.00"));

        Cpf cnpjEvento = new Cpf("98765432100"); 
        Conta contaRockInRio = new Conta("ID-002", cnpjEvento, new BigDecimal("0.00"));

        BigDecimal valorIngresso = new BigDecimal("500.00");
        Transacao transacao = new Transacao(contaFilipe, contaRockInRio, valorIngresso);

        // 2. Act (Agir)
        transacao.processar();

        // 3. Assert (Verificar)
        assertEquals(new BigDecimal("500.00"), contaFilipe.getSaldo(), "O saldo do comprador deve ser reduzido.");
        assertEquals(new BigDecimal("500.00"), contaRockInRio.getSaldo(), "O evento deve receber o valor.");
        assertEquals(StatusTransacao.CONCLUIDA, transacao.getStatus());
        assertNull(transacao.getMotivoFalha());
    }

    @Test
    public void deveFalharEmanterSaldosIntactosQuandoSaldoForInsuficiente() {
        // 1. Arrange (Preparar)
        Cpf cpfFilipe = new Cpf("12345678909");
        Conta contaFilipe = new Conta("ID-001", cpfFilipe, new BigDecimal("200.00"));

        Cpf cnpjEvento = new Cpf("98765432100");
        Conta contaRockInRio = new Conta("ID-002", cnpjEvento, new BigDecimal("0.00"));

        BigDecimal valorIngresso = new BigDecimal("500.00");
        Transacao transacao = new Transacao(contaFilipe, contaRockInRio, valorIngresso);

        // 2. Act (Agir)
        transacao.processar();

        // 3. Assert (Verificar)
        assertEquals(StatusTransacao.FALHADA, transacao.getStatus());
        assertNotNull(transacao.getMotivoFalha());
        assertEquals(new BigDecimal("200.00"), contaFilipe.getSaldo(), "O saldo não deve ser alterado.");
        assertEquals(new BigDecimal("0.00"), contaRockInRio.getSaldo(), "O evento não deve receber o valor.");
    }
}