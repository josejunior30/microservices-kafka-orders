package com.junior.NotaFiscal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.NotaFiscal.DTO.PagamentoConfirmadoEvent;

@Service
public class NotaFiscalConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotaFiscalConsumer.class);

    private final NotaFiscalService notaFiscalService;
    private final ObjectMapper mapper;

    public NotaFiscalConsumer(NotaFiscalService notaFiscalService, ObjectMapper mapper) {
        this.notaFiscalService = notaFiscalService;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "pagamento-confirmado", groupId ="nf-group")
    public void consumirPagamentoConfirmado(String mensagem) {
        try {
            logger.info("Mensagem recebida no tópico 'pagamento-confirmado': {}", mensagem);

            PagamentoConfirmadoEvent event = mapper.readValue(mensagem, PagamentoConfirmadoEvent.class);

         
            if (event.status() != null && !"CONFIRMADO".equalsIgnoreCase(event.status())) {
                logger.info("Ignorando status={} para pedidoId={}", event.status(), event.pedidoId());
                return;
            }

            notaFiscalService.gerarNotaFiscal(event.pedidoId(), event.valor());

            logger.info("Nota fiscal gerada com sucesso para Pedido ID={} | Valor={}",
                    event.pedidoId(), event.valor());

        } catch (Exception e) {
            logger.error("Erro ao processar mensagem: {}", mensagem, e);
            throw new RuntimeException(e); 
        }
    }
}
