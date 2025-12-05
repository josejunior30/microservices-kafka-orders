package com.junior.Pedido.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.Pedido.DTO.PagamentoConfirmadoEvent;

@Service
public class PedidoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PedidoConsumer.class);

    private final PedidoService pedidoService;
    private final ObjectMapper mapper;

    public PedidoConsumer(PedidoService pedidoService, ObjectMapper mapper) {
        this.pedidoService = pedidoService;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "pagamento-confirmado", groupId = "pedido-group")
    public void consumirPagamentoConfirmado(String mensagem) {
        try {
            logger.info("Mensagem recebida no tópico 'pagamento-confirmado': {}", mensagem);

            PagamentoConfirmadoEvent evento =
                    mapper.readValue(mensagem, PagamentoConfirmadoEvent.class);

           
            if (evento.status() != null && !"CONFIRMADO".equalsIgnoreCase(evento.status())) {
                logger.info("Ignorando status={} para pedidoId={}", evento.status(), evento.pedidoId());
                return;
            }

            pedidoService.atualizarStatusParaPago(evento.pedidoId());

            logger.info("Pedido ID={} atualizado para PAGO", evento.pedidoId());
        } catch (Exception e) {
            logger.error("Erro ao processar mensagem: {}", mensagem, e);
            throw new RuntimeException(e); 
        }
    }
}
