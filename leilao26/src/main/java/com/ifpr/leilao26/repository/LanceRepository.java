package com.ifpr.leilao26.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifpr.leilao26.model.Lance;

@Repository
public interface LanceRepository extends JpaRepository<Lance, Long>{
    // Histórico de lances de um leilão, do maior pro menor (o primeiro é o lance vencedor até o momento).
    List<Lance> findByLeilaoIdOrderByValorLanceDescDataHoraAsc(Long leilaoId);

    // Maior lance atual de um leilão (vazio se ainda não houve nenhum).
    Optional<Lance> findFirstByLeilaoIdOrderByValorLanceDesc(Long leilaoId);
}
