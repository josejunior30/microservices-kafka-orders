package com.junior.Pedido;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import com.junior.Pedido.DTO.PedidoDTO;
import com.junior.Pedido.entities.Pedido;
import com.junior.Pedido.entities.StatusPedido;
import com.junior.Pedido.exceptions.BusinessException;
import com.junior.Pedido.exceptions.ResourceNotFoundException;
import com.junior.Pedido.repository.PedidoRepository;
import com.junior.Pedido.service.PedidoService;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Test
    void findAll_deveRetornarListaDeDtos() {
        PedidoRepository repo = mock(PedidoRepository.class);
        PedidoService service = new PedidoService(repo);

        Pedido p1 = new Pedido(1L, LocalDateTime.now(), StatusPedido.REALIZADO, null, List.of());
        Pedido p2 = new Pedido(2L, LocalDateTime.now(), StatusPedido.PAGO, null, List.of());

        when(repo.findAll()).thenReturn(List.of(p1, p2));

        List<PedidoDTO> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repo).findAll();
    }

    @Test
    void findById_quandoExiste_deveRetornarDto() {
        PedidoRepository repo = mock(PedidoRepository.class);
        PedidoService service = new PedidoService(repo);

        Pedido pedido = new Pedido(10L, LocalDateTime.now(), StatusPedido.REALIZADO, null, List.of());
        when(repo.findById(10L)).thenReturn(Optional.of(pedido));

        PedidoDTO dto = service.findById(10L);

        assertNotNull(dto);
        verify(repo).findById(10L);
    }

    @Test
    void findById_quandoNaoExiste_deveLancarResourceNotFound() {
        PedidoRepository repo = mock(PedidoRepository.class);
        PedidoService service = new PedidoService(repo);

        when(repo.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex =
                assertThrows(ResourceNotFoundException.class, () -> service.findById(10L));

        assertTrue(ex.getMessage().contains("Pedido não encontrado"));
        verify(repo).findById(10L);
    }

    @Test
    void atualizarStatusParaPago_quandoNaoExiste_deveLancarResourceNotFound() {
        PedidoRepository repo = mock(PedidoRepository.class);
        PedidoService service = new PedidoService(repo);

        when(repo.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.atualizarStatusParaPago(10L));

        verify(repo).findById(10L);
        verify(repo, never()).save(any());
    }

    @Test
    void atualizarStatusParaPago_quandoStatusNaoForRealizado_deveLancarBusinessException() {
        PedidoRepository repo = mock(PedidoRepository.class);
        PedidoService service = new PedidoService(repo);

        Pedido pedido = new Pedido(10L, LocalDateTime.now(), StatusPedido.CONFIRMADO, null, List.of());
        when(repo.findById(10L)).thenReturn(Optional.of(pedido));

        BusinessException ex =
                assertThrows(BusinessException.class, () -> service.atualizarStatusParaPago(10L));

        assertTrue(ex.getMessage().contains("Não é possível mudar para PAGO"));
        verify(repo).findById(10L);
        verify(repo, never()).save(any());
    }

    @Test
    void atualizarStatusParaPago_quandoStatusForRealizado_deveSalvarComStatusPago() {
        PedidoRepository repo = mock(PedidoRepository.class);
        PedidoService service = new PedidoService(repo);

        Pedido pedido = new Pedido(10L, LocalDateTime.now(), StatusPedido.REALIZADO, null, List.of());
        when(repo.findById(10L)).thenReturn(Optional.of(pedido));

        // captura o que foi salvo pra garantir que mudou o status antes de salvar
        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        when(repo.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        PedidoDTO dto = service.atualizarStatusParaPago(10L);

        assertNotNull(dto);
        verify(repo).findById(10L);
        verify(repo).save(captor.capture());

        Pedido salvo = captor.getValue();
        assertEquals(StatusPedido.PAGO, salvo.getStatus());
    }
}