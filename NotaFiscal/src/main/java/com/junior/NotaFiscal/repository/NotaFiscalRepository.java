package com.junior.NotaFiscal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.junior.NotaFiscal.entities.NotaFiscal;

public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Long>{
	
}
