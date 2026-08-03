package com.ifpr.leilao26.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifpr.leilao26.model.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
    Pessoa findByUsername(String username);
    Pessoa findByEmail(String email);
}
