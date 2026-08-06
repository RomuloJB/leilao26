package com.ifpr.leilao26.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifpr.leilao26.model.Pagamento;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long>{
    List<Pagamento> findByStatus(String status);

    List<Pagamento> findByDataHora(LocalDateTime dataHora);
}
