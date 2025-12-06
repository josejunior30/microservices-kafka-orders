package com.junior.pagamento.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junior.pagamento.DTO.PagamentoDTO;
import com.junior.pagamento.entities.Pagamento;
import com.junior.pagamento.entities.StatusPagamento;
import com.junior.pagamento.exception.ResourceNotFoundException;
import com.junior.pagamento.repository.PagamentoRepository;

@Service
public class PagamentoService {

	private static final Logger logger = LoggerFactory.getLogger(PagamentoService.class);

	private final PagamentoRepository repository;
	private final PagamentoProducer pagamentoProducer;

	public PagamentoService(PagamentoRepository repository, PagamentoProducer pagamentoProducer) {
		this.repository = repository;
		this.pagamentoProducer = pagamentoProducer;
	}

	@Transactional(readOnly = true)
	public List<PagamentoDTO> findAll() {
		List<Pagamento> list = repository.findAll();
		logger.debug("Pagamentos encontrados: {}", list.size());

		return list.stream().map(PagamentoDTO::new).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public PagamentoDTO findById(Long id) {
		Pagamento entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado. ID=" + id));
		return new PagamentoDTO(entity);
	}

	@Transactional
	public PagamentoDTO confirma(Long id) {
		Pagamento pagamento = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado. ID=" + id));

		pagamento.setStatus(StatusPagamento.CONFIRMADO);
		Pagamento salvo = repository.save(pagamento);
		logger.info("Pagamento confirmado e salvo. ID={} pedidoId={} valor={}", salvo.getId(), salvo.getPedidoId(),
				salvo.getValor());
		pagamentoProducer.enviarPagamentoConfirmado(salvo);
		logger.info("Evento 'pagamento-confirmado' enviado. pagamentoId={} pedidoId={}", salvo.getId(),
				salvo.getPedidoId());
		return new PagamentoDTO(salvo);
	}
}