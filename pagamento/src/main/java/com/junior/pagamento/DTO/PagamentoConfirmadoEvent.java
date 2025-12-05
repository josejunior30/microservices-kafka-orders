package com.junior.pagamento.DTO;

import java.math.BigDecimal;

public record PagamentoConfirmadoEvent(Long pedidoId, String status, BigDecimal valor) {
}
