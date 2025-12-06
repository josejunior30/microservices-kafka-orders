package com.junior.NotaFiscal.controller;

import java.math.BigDecimal;
import java.util.List;
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

@RestController
@RequestMapping("/notas")
public class NotaFiscalController {

    private final NotaFiscalService service;
   
    public NotaFiscalController(NotaFiscalService service) {
        this.service = service;
  
    }
    
	@GetMapping
	public ResponseEntity<List<NotaFiscalDTO>> findAll(){
		List<NotaFiscalDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
    
	@PostMapping("/from-pedido/{pedidoId}")
    public NotaFiscalDTO gerar(
            @PathVariable Long pedidoId,
            @RequestParam(defaultValue = "00.00") BigDecimal valor
    ) throws Exception {
        return service.gerarNotaFiscal(pedidoId, valor);
    }

	 @GetMapping("/{id}/pdf")
	    public ResponseEntity<byte[]> baixarPdf(@PathVariable Long id) throws Exception {
	        byte[] bytes = service.obterPdf(id);
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"nota-" + id + ".pdf\"")
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(bytes);
	    }
}
