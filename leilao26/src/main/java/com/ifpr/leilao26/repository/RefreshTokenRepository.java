package com.ifpr.leilao26.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifpr.leilao26.model.RefreshToken;

@Repository 
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
    Optional<RefreshToken> findByToken(String token);
}
