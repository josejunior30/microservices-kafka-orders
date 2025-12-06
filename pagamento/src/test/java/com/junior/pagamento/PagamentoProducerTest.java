package com.junior.pagamento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.springframework.kafka.core.KafkaTemplate;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.pagamento.DTO.PagamentoConfirmadoEvent;
import com.junior.pagamento.entities.Pagamento;
import com.junior.pagamento.entities.StatusPagamento;
import com.junior.pagamento.service.PagamentoProducer;


@ExtendWith(MockitoExtension.class)
class PagamentoProducerTest {

    @Test
    void enviarPagamentoConfirmado_deveEnviarMensagemNoKafka() throws Exception {
        @SuppressWarnings("unchecked")
        KafkaTemplate<String, String> kafkaTemplate =
                (KafkaTemplate<String, String>) mock(KafkaTemplate.class);

        ObjectMapper objectMapper = mock(ObjectMapper.class);

        PagamentoProducer producer = new PagamentoProducer(kafkaTemplate, objectMapper);

        Pagamento pagamento = new Pagamento();
        pagamento.setPedidoId(10L);
        pagamento.setStatus(StatusPagamento.CONFIRMADO);
        pagamento.setValor(new BigDecimal("50.00"));

        String json = "{\"pedidoId\":10,\"status\":\"CONFIRMADO\",\"valor\":50.00}";
        when(objectMapper.writeValueAsString(any(PagamentoConfirmadoEvent.class))).thenReturn(json);

        producer.enviarPagamentoConfirmado(pagamento);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), msgCaptor.capture());

        assertEquals("pagamento-confirmado", topicCaptor.getValue());
        assertEquals("10", keyCaptor.getValue());
        assertEquals(json, msgCaptor.getValue());
    }

    @Test
    void enviarPagamentoConfirmado_quandoJsonFalhar_deveLancarRuntimeException() throws Exception {
        @SuppressWarnings("unchecked")
        KafkaTemplate<String, String> kafkaTemplate =
                (KafkaTemplate<String, String>) mock(KafkaTemplate.class);

        ObjectMapper objectMapper = mock(ObjectMapper.class);

        PagamentoProducer producer = new PagamentoProducer(kafkaTemplate, objectMapper);

        Pagamento pagamento = new Pagamento();
        pagamento.setPedidoId(10L);
        pagamento.setStatus(StatusPagamento.CONFIRMADO);
        pagamento.setValor(new BigDecimal("50.00"));

        when(objectMapper.writeValueAsString(any(PagamentoConfirmadoEvent.class)))
                .thenThrow(new JsonProcessingException("erro json") {});

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> producer.enviarPagamentoConfirmado(pagamento));

        assertEquals("Erro ao enviar mensagem Kafka", ex.getMessage());
        verifyNoInteractions(kafkaTemplate);
    }

    @Test
    void enviarPagamentoConfirmado_quandoKafkaFalhar_deveLancarRuntimeException() throws Exception {
        @SuppressWarnings("unchecked")
        KafkaTemplate<String, String> kafkaTemplate =
                (KafkaTemplate<String, String>) mock(KafkaTemplate.class);

        ObjectMapper objectMapper = mock(ObjectMapper.class);

        PagamentoProducer producer = new PagamentoProducer(kafkaTemplate, objectMapper);

        Pagamento pagamento = new Pagamento();
        pagamento.setPedidoId(10L);
        pagamento.setStatus(StatusPagamento.CONFIRMADO);
        pagamento.setValor(new BigDecimal("50.00"));

        String json = "{\"pedidoId\":10,\"status\":\"CONFIRMADO\",\"valor\":50.00}";
        when(objectMapper.writeValueAsString(any(PagamentoConfirmadoEvent.class))).thenReturn(json);

        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("kafka down"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> producer.enviarPagamentoConfirmado(pagamento));

        assertEquals("Erro ao enviar mensagem Kafka", ex.getMessage());
    }
}