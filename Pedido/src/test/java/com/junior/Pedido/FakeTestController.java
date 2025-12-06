package com.junior.Pedido;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junior.Pedido.exceptions.BusinessException;
import com.junior.Pedido.exceptions.ResourceNotFoundException;

@RestController
public class FakeTestController {

    @GetMapping("/test/notfound")
    public void notfound() {
        throw new ResourceNotFoundException("Pedido não encontrado. ID=10");
    }

    @GetMapping("/test/business")
    public void business() {
        throw new BusinessException("Não pode mudar status");
    }

    @GetMapping("/test/integrity")
    public void integrity() {
        throw new DataIntegrityViolationException("fk fail");
    }
}