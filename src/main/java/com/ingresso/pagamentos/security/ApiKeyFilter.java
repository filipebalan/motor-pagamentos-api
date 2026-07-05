package com.ingresso.pagamentos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    // Em um projeto real, essa chave ficaria escondida nas variáveis de ambiente do servidor
    private static final String NOME_CABECHALHO = "X-API-KEY";
    private static final String CHAVE_SECRETA = "pagamentos-api-key-2026-super-secreta";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String caminhoRequisicao = request.getRequestURI();

        // 1. Libera as portas públicas (Swagger e Banco de Dados em Memória)
        if (caminhoRequisicao.startsWith("/swagger-ui") || 
            caminhoRequisicao.startsWith("/v3/api-docs") || 
            caminhoRequisicao.startsWith("/h2-console")) {
            
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Verifica a Chave de API nas rotas protegidas
        String chaveRecebida = request.getHeader(NOME_CABECHALHO);

        if (CHAVE_SECRETA.equals(chaveRecebida)) {
            // Chave correta: Deixa passar para o Controller
            filterChain.doFilter(request, response);
        } else {
            // Chave incorreta ou ausente: Bloqueia na porta (HTTP 401)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": 401, \"erro\": \"Acesso negado. API Key invalida ou ausente.\"}");
        }
    }
}