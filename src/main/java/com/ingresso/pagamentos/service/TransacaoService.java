package com.ingresso.pagamentos.service;

import com.ingresso.pagamentos.domain.Conta;
import com.ingresso.pagamentos.repository.ContaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransacaoService {

    private final ContaRepository contaRepository;

    // A injeção de dependência via construtor é o padrão ouro no Spring
    public TransacaoService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    @Transactional
    public void realizarTransferencia(String idContaOrigem, String idContaDestino, BigDecimal valor) {
        
        // 1. O Hibernate vai ao banco, busca a conta e faz um "SELECT ... FOR UPDATE" (Pessimistic Lock)
        Conta contaOrigem = contaRepository.findByIdWithLock(idContaOrigem)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada."));

        Conta contaDestino = contaRepository.findByIdWithLock(idContaDestino)
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada."));

        // 2. Executamos a regra de negócio blindada que criamos na Etapa 1
        contaOrigem.debitar(valor);
        contaDestino.creditar(valor);

        // 3. Salvamos o novo estado no banco de dados
        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);
        
        // O @Transactional garante que, se qualquer coisa falhar até este ponto,
        // o banco de dados faz um ROLLBACK automático e desfaz tudo.
    }
}