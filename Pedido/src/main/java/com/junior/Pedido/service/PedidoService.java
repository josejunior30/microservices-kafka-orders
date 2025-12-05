package com.junior.Pedido.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.junior.Pedido.DTO.PedidoDTO;
import com.junior.Pedido.entities.Pedido;
import com.junior.Pedido.entities.StatusPedido;
import com.junior.Pedido.repository.PedidoRepository;

@Service
public class PedidoService {
	    private final PedidoRepository repository;

	    public PedidoService(PedidoRepository repository) {
	        this.repository = repository;
	    }

	@Transactional(readOnly = true)
	public List<PedidoDTO> findAll() {
		List<Pedido> list = repository.findAll();
		return list.stream()
		           .map(x -> new PedidoDTO(x))
		           .collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public PedidoDTO findById(Long id) {
		Pedido entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
		return new PedidoDTO(entity);
	}

	 @Transactional
	    public PedidoDTO atualizarStatusParaPago(Long id) {
	        Pedido pedido = repository.findById(id)
	                .orElseThrow(() -> {
	                    return new RuntimeException("Pedido não encontrado");
	                });

	        pedido.setStatus(StatusPedido.PAGO);
	        Pedido salvo = repository.save(pedido);
	        return new PedidoDTO(salvo);
	    }
}
