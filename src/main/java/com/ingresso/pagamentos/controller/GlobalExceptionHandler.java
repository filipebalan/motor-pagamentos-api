package com.ingresso.pagamentos.controller;

import com.ingresso.pagamentos.exception.SaldoInsuficienteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Captura erros de Regra de Negócio (ex: Saldo Insuficiente)
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<Map<String, String>> handleSaldoInsuficiente(SaldoInsuficienteException ex) {
        Map<String, String> respostaErro = new HashMap<>();
        respostaErro.put("erro", ex.getMessage());
        
        // Retorna 422 (Unprocessable Entity) utilizando o código HTTP puro
        return ResponseEntity.status(422).body(respostaErro);
    }

    // 2. Captura erros de Validação Básica (ex: IllegalArgumentException que colocamos na Conta)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> respostaErro = new HashMap<>();
        respostaErro.put("erro", ex.getMessage());
        
        // Retorna 400 (Bad Request)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respostaErro);
    }

    // 3. Captura os erros das nossas anotações do DTO (@NotBlank, @Positive)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            erros.put(error.getField(), error.getDefaultMessage())
        );
        
        // Retorna 400 (Bad Request) com a lista dos campos que falharam
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
    }
}