package com.ingresso.pagamentos.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProtocoloService {

    // Simula um banco de dados em memória (Cache) Thread-Safe para alta concorrência
    private final Map<String, String> cacheProtocolos = new ConcurrentHashMap<>();

    public void registrar(String protocolo, String status) {
        cacheProtocolos.put(protocolo, status);
    }

    public String consultar(String protocolo) {
        return cacheProtocolos.getOrDefault(protocolo, "PROTOCOLO_NAO_ENCONTRADO");
    }
}