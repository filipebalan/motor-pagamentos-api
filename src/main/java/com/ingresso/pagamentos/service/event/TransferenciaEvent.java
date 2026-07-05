package com.ingresso.pagamentos.service.event;

import java.math.BigDecimal;

public record TransferenciaEvent(
    String idProtocolo,
    String idContaOrigem,
    String idContaDestino,
    BigDecimal valor
) {}