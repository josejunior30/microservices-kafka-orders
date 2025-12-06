package com.junior.pagamento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import com.junior.pagamento.DTO.PagamentoDTO;
import com.junior.pagamento.entities.Pagamento;
import com.junior.pagamento.entities.StatusPagamento;
import com.junior.pagamento.repository.PagamentoRepository;
import com.junior.pagamento.service.PagamentoProducer;
import com.junior.pagamento.service.PagamentoService;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Test
    void findAll_deveRetornarListaDeDtos() {
        PagamentoRepository repo = mock(PagamentoRepository.class);
        PagamentoProducer producer = mock(PagamentoProducer.class);
        PagamentoService service = new PagamentoService(repo, producer);

        Pagamento p1 = new Pagamento(1L, new BigDecimal("10.00"), StatusPagamento.PENDENTE, 100L);
        Pagamento p2 = new Pagamento(2L, new BigDecimal("20.00"), StatusPagamento.PENDENTE, 101L);

        when(repo.findAll()).thenReturn(List.of(p1, p2));

        List<PagamentoDTO> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repo).findAll();
        verifyNoInteractions(producer);
    }

    @Test
    void findById_quandoExiste_deveRetornarDto() {
        PagamentoRepository repo = mock(PagamentoRepository.class);
        PagamentoProducer producer = mock(PagamentoProducer.class);
        PagamentoService service = new PagamentoService(repo, producer);

        Pagamento p = new Pagamento(10L, new BigDecimal("50.00"), StatusPagamento.PENDENTE, 200L);
        when(repo.findById(10L)).thenReturn(Optional.of(p));

        PagamentoDTO dto = service.findById(10L);

        assertNotNull(dto);
        verify(repo).findById(10L);
        verifyNoInteractions(producer);
    }

    @Test
    void findById_quandoNaoExiste_deveLancarExcecao() {
        PagamentoRepository repo = mock(PagamentoRepository.class);
        PagamentoProducer producer = mock(PagamentoProducer.class);
        PagamentoService service = new PagamentoService(repo, producer);

        when(repo.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.findById(10L));
        assertEquals("Pagamento não encontrado", ex.getMessage());

        verify(repo).findById(10L);
        verifyNoInteractions(producer);
    }

    @Test
    void confirma_quandoExiste_deveSalvarStatusConfirmado_eEnviarEvento() {
        PagamentoRepository repo = mock(PagamentoRepository.class);
        PagamentoProducer producer = mock(PagamentoProducer.class);
        PagamentoService service = new PagamentoService(repo, producer);

        Pagamento pagamento = new Pagamento(10L, new BigDecimal("50.00"), StatusPagamento.PENDENTE, 300L);

        when(repo.findById(10L)).thenReturn(Optional.of(pagamento));
        when(repo.save(any(Pagamento.class))).thenAnswer(inv -> inv.getArgument(0));

        PagamentoDTO dto = service.confirma(10L);

        assertNotNull(dto);

        // garante que salvou com status alterado
        ArgumentCaptor<Pagamento> captor = ArgumentCaptor.forClass(Pagamento.class);
        verify(repo).save(captor.capture());
        assertEquals(StatusPagamento.CONFIRMADO, captor.getValue().getStatus());

        // garante que enviou evento com o "salvo"
        verify(producer).enviarPagamentoConfirmado(any(Pagamento.class));

        verify(repo).findById(10L);
    }

    @Test
    void confirma_quandoNaoExiste_deveLancarExcecao_eNaoSalvarNemEnviar() {
        PagamentoRepository repo = mock(PagamentoRepository.class);
        PagamentoProducer producer = mock(PagamentoProducer.class);
        PagamentoService service = new PagamentoService(repo, producer);

        when(repo.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.confirma(10L));
        assertEquals("Pagamento não encontrado", ex.getMessage());

        verify(repo).findById(10L);
        verify(repo, never()).save(any());
        verifyNoInteractions(producer);
    }
}