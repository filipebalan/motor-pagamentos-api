package com.ingresso.pagamentos.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveRetornarStatus202EProtocolo_QuandoReceberTransferenciaValidaComApiKey() throws Exception {
        
        String jsonRequest = """
                {
                    "idContaOrigem": "ID-001",
                    "idContaDestino": "ID-002",
                    "valor": 500.00
                }
                """;

        mockMvc.perform(post("/api/v1/transferencias")
                // Apresentando a nossa carteirinha de segurança para o Filtro!
                .header("X-API-KEY", "pagamentos-api-key-2026-super-secreta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.protocolo").exists())
                .andExpect(jsonPath("$.mensagem").value("Transação aceita e enviada para processamento."));
    }
}