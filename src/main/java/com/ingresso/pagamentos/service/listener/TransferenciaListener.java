package com.ingresso.pagamentos.service.listener;

import com.ingresso.pagamentos.service.ProtocoloService;
import com.ingresso.pagamentos.service.TransacaoService;
import com.ingresso.pagamentos.service.event.TransferenciaEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TransferenciaListener {

    private final TransacaoService transacaoService;
    private final ProtocoloService protocoloService;

    // Injetamos o nosso novo serviço de cache
    public TransferenciaListener(TransacaoService transacaoService, ProtocoloService protocoloService) {
        this.transacaoService = transacaoService;
        this.protocoloService = protocoloService;
    }

    @Async
    @EventListener
    public void processarTransferencia(TransferenciaEvent event) {
        try {
            transacaoService.realizarTransferencia(
                event.idContaOrigem(),
                event.idContaDestino(),
                event.valor()
            );
            
            // Se passou direto pelo método acima, foi um sucesso
            protocoloService.registrar(event.idProtocolo(), "CONCLUIDA_COM_SUCESSO");
            System.out.println("Protocolo " + event.idProtocolo() + " finalizado com sucesso.");
            
        } catch (Exception e) {
            // Se a regra de negócio do DDD falhar (ex: Saldo Insuficiente)
            protocoloService.registrar(event.idProtocolo(), "FALHADA: " + e.getMessage());
            System.err.println("Protocolo " + event.idProtocolo() + " falhou.");
        }
    }
}