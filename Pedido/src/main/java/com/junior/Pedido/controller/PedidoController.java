package com.junior.Pedido.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junior.Pedido.DTO.PedidoDTO;
import com.junior.Pedido.service.PedidoService;

@RestController
@RequestMapping("/pedido")
public class PedidoController {
	private final PedidoService service;
	
	  public PedidoController(PedidoService service) {
		this.service = service;
	}

	  @GetMapping
		public ResponseEntity<List<PedidoDTO>> findAll(){
			List<PedidoDTO> list = service.findAll();
			return ResponseEntity.ok().body(list);
		}
	  
	  @GetMapping(value="/{id}")
		public ResponseEntity<PedidoDTO>findById(@PathVariable Long id){
		  PedidoDTO user = service.findById(id);
			return ResponseEntity.ok().body(user);
		}
}
