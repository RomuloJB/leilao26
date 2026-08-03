package com.ifpr.leilao26.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifpr.leilao26.enums.StatusLeilao;
import com.ifpr.leilao26.model.Leilao;

@Repository
public interface LeilaoRepository extends JpaRepository<Leilao, Long>{
    Leilao findByTitulo(String titulo);
    
    Leilao findByDataHoraInicio(LocalDateTime dataHoraInicio);
    
    Leilao findByDataHoraFim(LocalDateTime dataHoraFim);

    Leilao findByStatus(StatusLeilao status);

    Leilao findByValorIncremento(Float valorIncremento);

    Leilao findByLanceMinimo(Float lanceMinimo);
}
