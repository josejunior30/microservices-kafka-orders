package com.junior.pagamento.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.pagamento.DTO.PagamentoConfirmadoEvent;
import com.junior.pagamento.entities.Pagamento;

@Service
public class PagamentoProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public PagamentoProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
	}

	public void enviarPagamentoConfirmado(Pagamento pagamento) {
		try {
			PagamentoConfirmadoEvent event = new PagamentoConfirmadoEvent(pagamento.getPedidoId(),
					pagamento.getStatus().name(), pagamento.getValor());
			String message = objectMapper.writeValueAsString(event);

			kafkaTemplate.send("pagamento-confirmado", pagamento.getPedidoId().toString(), message);

			System.out.println("📤 Evento enviado para Kafka: " + message);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao enviar mensagem Kafka", e);
		}
	}
}
