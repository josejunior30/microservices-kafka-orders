package com.junior.Pedido.entities;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_pedido")
public class Pedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime dataHora;
	
	@Enumerated(EnumType.STRING)
	private StatusPedido status;

	@Embedded
	private Cliente cliente;

	@OneToMany(mappedBy = "pedido")
	private List<ItemPedido> itensPedido;

	public Pedido() {

	}

	public Pedido(Long id, LocalDateTime dataHora, StatusPedido status, Cliente cliente, List<ItemPedido> itensPedido) {
		super();
		this.id = id;
		this.dataHora = dataHora;
		this.status = status;
		this.cliente = cliente;
		this.itensPedido = itensPedido;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public StatusPedido getStatus() {
		return status;
	}

	public void setStatus(StatusPedido status) {
		this.status = status;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<ItemPedido> getItensPedido() {
		return itensPedido;
	}

	public void setItensPedido(List<ItemPedido> itensPedido) {
		this.itensPedido = itensPedido;
	}

}