package com.junior.pagamento.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.pagamento.DTO.PagamentoConfirmadoEvent;
import com.junior.pagamento.entities.Pagamento;

@Service
public class PagamentoProducer {
	private static final Logger logger = LoggerFactory.getLogger(PagamentoProducer.class);
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
		} catch (Exception e) {
			logger.warn("Falha ao enviar evento para Kafka (ignorando). pedidoId={} motivo={}",
					pagamento != null ? pagamento.getPedidoId() : null, e.getMessage());
		}
	}

}
