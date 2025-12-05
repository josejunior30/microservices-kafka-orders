package com.junior.Pedido.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Cliente {

	@Column(name = "nome_cliente")
	public String nome;

	@Column(name = "cpf_cliente")
	public String cpf;

	@Column(name = "celular_cliente")
	public String celular;

	@Column(name = "endereco_cliente")
	public String endereco;

}