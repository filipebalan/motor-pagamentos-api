package com.ingresso.pagamentos.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record TransferenciaRequest(
    
    @NotBlank(message = "O ID da conta de origem é obrigatório.")
    String idContaOrigem,

    @NotBlank(message = "O ID da conta de destino é obrigatório.")
    String idContaDestino,

    @NotNull(message = "O valor da transferência é obrigatório.")
    @Positive(message = "O valor da transferência deve ser estritamente positivo.")
    BigDecimal valor

) {}