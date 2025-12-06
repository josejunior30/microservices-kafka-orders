package com.junior.NotaFiscal.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junior.NotaFiscal.DTO.NotaFiscalDTO;
import com.junior.NotaFiscal.entities.NotaFiscal;
import com.junior.NotaFiscal.repository.NotaFiscalRepository;

@Service
public class NotaFiscalService {

	private final NotaFiscalRepository repository;

	public NotaFiscalService(NotaFiscalRepository repository) {
		this.repository = repository;

	}

	@Transactional(readOnly = true)
	public List<NotaFiscalDTO> findAll() {
		List<NotaFiscal> list = repository.findAll();
		return list.stream().map(x -> new NotaFiscalDTO(x)).collect(Collectors.toList());
	}

}