package com.junior.Pedido;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.Pedido.DTO.PagamentoConfirmadoEvent;
import com.junior.Pedido.service.PedidoConsumer;
import com.junior.Pedido.service.PedidoService;

@ExtendWith(MockitoExtension.class)
class PedidoConsumerTest {

    @Test
    void deveIgnorarQuandoStatusNaoForConfirmado() throws Exception {
        PedidoService service = mock(PedidoService.class);
        ObjectMapper mapper = mock(ObjectMapper.class);
        PedidoConsumer consumer = new PedidoConsumer(service, mapper);

        PagamentoConfirmadoEvent evento = new PagamentoConfirmadoEvent(10L, "CANCELADO", new BigDecimal("50.00"));
        when(mapper.readValue(anyString(), eq(PagamentoConfirmadoEvent.class))).thenReturn(evento);

        assertDoesNotThrow(() -> consumer.consumirPagamentoConfirmado("{\"pedidoId\":10,\"status\":\"CANCELADO\",\"valor\":50}"));

        verify(service, never()).atualizarStatusParaPago(anyLong());
    }

    @Test
    void deveAtualizarParaPagoQuandoStatusForConfirmado() throws Exception {
        PedidoService service = mock(PedidoService.class);
        ObjectMapper mapper = mock(ObjectMapper.class);
        PedidoConsumer consumer = new PedidoConsumer(service, mapper);

        PagamentoConfirmadoEvent evento = new PagamentoConfirmadoEvent(10L, "CONFIRMADO", new BigDecimal("50.00"));
        when(mapper.readValue(anyString(), eq(PagamentoConfirmadoEvent.class))).thenReturn(evento);

        assertDoesNotThrow(() -> consumer.consumirPagamentoConfirmado("{\"pedidoId\":10,\"status\":\"CONFIRMADO\",\"valor\":50}"));

        verify(service).atualizarStatusParaPago(10L);
    }

    @Test
    void deveAtualizarMesmoQuandoStatusForNull() throws Exception {
        PedidoService service = mock(PedidoService.class);
        ObjectMapper mapper = mock(ObjectMapper.class);
        PedidoConsumer consumer = new PedidoConsumer(service, mapper);

        PagamentoConfirmadoEvent evento = new PagamentoConfirmadoEvent(10L, null, new BigDecimal("50.00"));
        when(mapper.readValue(anyString(), eq(PagamentoConfirmadoEvent.class))).thenReturn(evento);

        assertDoesNotThrow(() -> consumer.consumirPagamentoConfirmado("{\"pedidoId\":10,\"status\":null,\"valor\":50}"));

        verify(service).atualizarStatusParaPago(10L);
    }

    @Test
    void naoDeveLancarExcecaoQuandoJsonForInvalido() throws Exception {
        PedidoService service = mock(PedidoService.class);
        ObjectMapper mapper = mock(ObjectMapper.class);
        PedidoConsumer consumer = new PedidoConsumer(service, mapper);

        when(mapper.readValue(anyString(), eq(PagamentoConfirmadoEvent.class)))
                .thenThrow(new JsonProcessingException("json ruim") {});

        assertDoesNotThrow(() -> consumer.consumirPagamentoConfirmado("JSON QUEBRADO"));

        verify(service, never()).atualizarStatusParaPago(anyLong());
    }

    @Test
    void naoDeveLancarExcecaoQuandoServiceFalhar() throws Exception {
        PedidoService service = mock(PedidoService.class);
        ObjectMapper mapper = mock(ObjectMapper.class);
        PedidoConsumer consumer = new PedidoConsumer(service, mapper);

        PagamentoConfirmadoEvent evento = new PagamentoConfirmadoEvent(10L, "CONFIRMADO", new BigDecimal("50.00"));
        when(mapper.readValue(anyString(), eq(PagamentoConfirmadoEvent.class))).thenReturn(evento);

        doThrow(new RuntimeException("falhou")).when(service).atualizarStatusParaPago(10L);

        assertDoesNotThrow(() -> consumer.consumirPagamentoConfirmado("{\"pedidoId\":10,\"status\":\"CONFIRMADO\",\"valor\":50}"));
    }
}