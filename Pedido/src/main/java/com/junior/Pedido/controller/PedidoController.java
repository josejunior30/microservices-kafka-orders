package com.junior.Pedido.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junior.Pedido.DTO.ApiError;
import com.junior.Pedido.DTO.PedidoDTO;
import com.junior.Pedido.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pedidos", description = "Endpoints do microserviço de Pedido")
@RestController
@RequestMapping("/pedido")
public class PedidoController {
	private final PedidoService service;

	public PedidoController(PedidoService service) {
		this.service = service;
	}

	@Operation(description = "Retorna todos os pedidos cadastrados com itens e cardápio.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class))) })
	@GetMapping
	public ResponseEntity<List<PedidoDTO>> findAll() {
		List<PedidoDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

	@Operation(description = "Retorna um pedido pelo ID (com itens).")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Pedido encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class))),
			@ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))) })
	@GetMapping(value = "/{id}")
	public ResponseEntity<PedidoDTO> findById(@PathVariable Long id) {
		PedidoDTO user = service.findById(id);
		return ResponseEntity.ok().body(user);
	}
}
