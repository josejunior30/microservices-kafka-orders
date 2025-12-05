package com.junior.Pedido.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.junior.Pedido.entities.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{
	   @EntityGraph(attributePaths = {
	            "itensPedido",
	            "itensPedido.itemCardapio"
	    })
	    Optional<Pedido> findById(Long id);


	   @EntityGraph(attributePaths = {
	            "itensPedido",
	            "itensPedido.itemCardapio"
	    })
	    List<Pedido> findAll();
	}


