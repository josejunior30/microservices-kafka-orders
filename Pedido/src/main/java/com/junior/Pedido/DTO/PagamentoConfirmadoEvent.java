package com.junior.Pedido.DTO;

import java.math.BigDecimal;

public record PagamentoConfirmadoEvent(Long pedidoId, String status, BigDecimal valor) {}

