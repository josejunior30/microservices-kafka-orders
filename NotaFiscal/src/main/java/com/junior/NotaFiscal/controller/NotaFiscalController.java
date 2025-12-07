package com.junior.NotaFiscal.controller;

import java.math.BigDecimal;
import java.util.List;

import org.apache.kafka.common.requests.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.junior.NotaFiscal.DTO.NotaFiscalDTO;
import com.junior.NotaFiscal.service.NotaFiscalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Notas Fiscais", description = "Endpoints do microserviço de Nota Fiscal (geração de XML e PDF)")
@RestController
@RequestMapping("/notas")
public class NotaFiscalController {

	private final NotaFiscalService service;

	public NotaFiscalController(NotaFiscalService service) {
		this.service = service;

	}

	@Operation(description = "Retorna todas as notas fiscais cadastradas.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotaFiscalDTO.class))) })
	@GetMapping
	public ResponseEntity<List<NotaFiscalDTO>> findAll() {
		List<NotaFiscalDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

	@Operation(description = "Gera (ou reaproveita) a nota fiscal para um pedidoId. Se já existir, tenta garantir que o PDF esteja gerado.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Nota fiscal gerada/retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotaFiscalDTO.class))),
			@ApiResponse(responseCode = "409", description = "Conflito/violação de integridade (ex.: idempotência/unique)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "Erro inesperado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))) })
	@PostMapping("/from-pedido/{pedidoId}")
	public NotaFiscalDTO gerar(@PathVariable Long pedidoId, @RequestParam(defaultValue = "00.00") BigDecimal valor)
			throws Exception {
		return service.gerarNotaFiscal(pedidoId, valor);
	}

	@Operation(description = "Baixa o DANFE (PDF) da nota fiscal informada.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "PDF retornado com sucesso", content = @Content(mediaType = "application/pdf")),
			@ApiResponse(responseCode = "404", description = "Nota não encontrada / PDF não disponível", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))) })
	@GetMapping("/{id}/pdf")
	public ResponseEntity<byte[]> baixarPdf(@PathVariable Long id) throws Exception {
		byte[] bytes = service.obterPdf(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"nota-" + id + ".pdf\"")
				.contentType(MediaType.APPLICATION_PDF).body(bytes);
	}
}
