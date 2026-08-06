package com.ifpr.leilao26.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifpr.leilao26.model.Lance;

@Repository
public interface LanceRepository extends JpaRepository<Lance, Long>{
    Lance findByValorLance(Float valorLance);
    
    Lance findByDataHora(LocalDateTime dataHora);
}
