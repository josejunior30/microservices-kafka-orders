package com.junior.pagamento.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junior.pagamento.DTO.PagamentoDTO;
import com.junior.pagamento.service.PagamentoService;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {
	
    private final PagamentoService service;

    public PagamentoController(PagamentoService service) {
        this.service = service;
    } 
    @GetMapping
    public ResponseEntity<List<PagamentoDTO>> findAll() {
        List<PagamentoDTO> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> findById(@PathVariable Long id) {
        PagamentoDTO pagamento = service.findById(id);
        return ResponseEntity.ok(pagamento);
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<PagamentoDTO> confirmar(@PathVariable Long id) {
        PagamentoDTO dto = service.confirma(id);
        return ResponseEntity.ok(dto);
    }
    
}
