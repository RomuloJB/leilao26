package com.ifpr.leilao26.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifpr.leilao26.model.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
    Pessoa findByUsername(String username);
    Pessoa findByEmail(String email);

    @Query("SELECT DISTINCT p FROM Pessoa p " +
           "LEFT JOIN FETCH p.perfis pp " +
           "LEFT JOIN FETCH pp.perfil " +
           "WHERE p.username = :username")
    Optional<Pessoa> findByUsernameComPerfis(@Param("username") String username);
}
