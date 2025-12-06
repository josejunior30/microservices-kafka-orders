package com.junior.NotaFiscal.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.junior.NotaFiscal.entities.NotaFiscal;

public class NotaFiscalDTO {
	private Long id;
	private Long pedidoId;
	private int numero;
	private int serie;
	private LocalDateTime dataEmissao;
	private BigDecimal valorTotal;
	private String chaveAcesso;
	
	public NotaFiscalDTO(){
		
	}
	
	public NotaFiscalDTO(NotaFiscal entity){
		id=entity.getId();
		pedidoId=entity.getPedidoId();
		numero=entity.getNumero();
		serie=entity.getSerie();
		dataEmissao=entity.getDataEmissao();
		valorTotal=entity.getValorTotal();
		chaveAcesso=entity.getChaveAcesso();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getSerie() {
		return serie;
	}

	public void setSerie(int serie) {
		this.serie = serie;
	}

	public LocalDateTime getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(LocalDateTime dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getChaveAcesso() {
		return chaveAcesso;
	}

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}
	
	
}
