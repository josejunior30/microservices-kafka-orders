package com.junior.pagamento.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.junior.pagamento.DTO.PagamentoDTO;
import com.junior.pagamento.entities.Pagamento;
import com.junior.pagamento.entities.StatusPagamento;
import com.junior.pagamento.repository.PagamentoRepository;


@Service
public class PagamentoService {
	private final PagamentoRepository repository;
	private final PagamentoProducer pagamentoProducer;

	public PagamentoService(PagamentoRepository repository, PagamentoProducer pagamentoProducer) {
		this.repository = repository;
		this.pagamentoProducer = pagamentoProducer;
	}

	@Transactional(readOnly = true)
	public List<PagamentoDTO> findAll() {
		List<Pagamento> list = repository.findAll();
		return list.stream().map(x -> new PagamentoDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public PagamentoDTO findById(Long id) {
		Pagamento entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
		return new PagamentoDTO(entity);
	}
	@Transactional
	public PagamentoDTO confirma(Long id) {
		Pagamento pagamento = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

		pagamento.setStatus(StatusPagamento.CONFIRMADO);
		Pagamento salvo = repository.save(pagamento);
		pagamentoProducer.enviarPagamentoConfirmado(salvo);

		return new PagamentoDTO(salvo);
	}

}