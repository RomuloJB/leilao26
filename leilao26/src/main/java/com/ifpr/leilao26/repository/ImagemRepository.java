package com.ifpr.leilao26.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifpr.leilao26.model.Imagem;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Long>{
}
