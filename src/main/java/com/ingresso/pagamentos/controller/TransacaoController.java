package com.ingresso.pagamentos.controller;

import com.ingresso.pagamentos.controller.dto.TransferenciaRequest;
import com.ingresso.pagamentos.service.ProtocoloService;
import com.ingresso.pagamentos.service.event.TransferenciaEvent;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transferencias")
public class TransacaoController {

    private final ApplicationEventPublisher eventPublisher;
    private final ProtocoloService protocoloService;

    public TransacaoController(ApplicationEventPublisher eventPublisher, ProtocoloService protocoloService) {
        this.eventPublisher = eventPublisher;
        this.protocoloService = protocoloService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> realizarTransferencia(@RequestBody @Valid TransferenciaRequest request) {
        
        String protocolo = UUID.randomUUID().toString();
        
        protocoloService.registrar(protocolo, "PROCESSANDO");
        
        TransferenciaEvent mensagem = new TransferenciaEvent(
            protocolo, request.idContaOrigem(), request.idContaDestino(), request.valor()
        );

        eventPublisher.publishEvent(mensagem);

        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Transação aceita e enviada para processamento.");
        resposta.put("protocolo", protocolo);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(resposta);
    }

    @GetMapping("/{protocolo}")
    public ResponseEntity<Map<String, String>> consultarProtocolo(@PathVariable String protocolo) {
        
        String status = protocoloService.consultar(protocolo);
        
        Map<String, String> resposta = new HashMap<>();
        resposta.put("protocolo", protocolo);
        resposta.put("status", status);

        if (status.equals("PROTOCOLO_NAO_ENCONTRADO")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
        }

        return ResponseEntity.ok(resposta);
    }
}