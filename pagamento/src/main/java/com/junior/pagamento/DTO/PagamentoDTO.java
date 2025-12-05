package com.junior.pagamento.DTO;

import java.math.BigDecimal;

import com.junior.pagamento.entities.Pagamento;
import com.junior.pagamento.entities.StatusPagamento;

public class PagamentoDTO {
	private Long id;
	private BigDecimal valor;
	private StatusPagamento status;
	private Long pedidoId;
	
	public PagamentoDTO() {
		
		
	}

	public PagamentoDTO(Long id, BigDecimal valor, StatusPagamento status, Long pedidoId) {
		super();
		this.id = id;
		this.valor = valor;
		this.status = status;
		this.pedidoId = pedidoId;
	}
	public PagamentoDTO(Pagamento entity) {
	
		id = entity.getId();
		valor = entity.getValor();
		status = entity.getStatus();
		pedidoId = entity.getPedidoId();
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public StatusPagamento getStatus() {
		return status;
	}

	public void setStatus(StatusPagamento status) {
		this.status = status;
	}

	public Long getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
	}
	
	
}
