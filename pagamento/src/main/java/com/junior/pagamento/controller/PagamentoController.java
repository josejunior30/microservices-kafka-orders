package com.junior.pagamento.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junior.pagamento.DTO.ApiError;
import com.junior.pagamento.DTO.PagamentoDTO;
import com.junior.pagamento.service.PagamentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pagamentos", description = "Endpoints do microserviço de Pagamento")
@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

	private final PagamentoService service;

	public PagamentoController(PagamentoService service) {
		this.service = service;
	}

	@Operation(description = "Retorna todos os pagamentos cadastrados.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagamentoDTO.class))) })
	@GetMapping
	public ResponseEntity<List<PagamentoDTO>> findAll() {
		List<PagamentoDTO> list = service.findAll();
		return ResponseEntity.ok(list);
	}

	@Operation(description = "Retorna um pagamento pelo ID.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Pagamento encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagamentoDTO.class))),
			@ApiResponse(responseCode = "404", description = "Pagamento não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))) })
	@GetMapping("/{id}")
	public ResponseEntity<PagamentoDTO> findById(@PathVariable Long id) {
		PagamentoDTO pagamento = service.findById(id);
		return ResponseEntity.ok(pagamento);
	}

	@Operation(description = "Confirma o pagamento e publica um evento no Kafka.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Pagamento confirmado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagamentoDTO.class))),
			@ApiResponse(responseCode = "404", description = "Pagamento não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "422", description = "Regra de negócio violada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))) })
	@PatchMapping("/{id}/confirmar")
	public ResponseEntity<PagamentoDTO> confirmar(@PathVariable Long id) {
		PagamentoDTO dto = service.confirma(id);
		return ResponseEntity.ok(dto);
	}

}
