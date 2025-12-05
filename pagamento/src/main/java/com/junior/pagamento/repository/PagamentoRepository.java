package com.junior.pagamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.junior.pagamento.entities.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long>{

}
