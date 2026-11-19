package com.ifpr.leilao26.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.model.RefreshToken;
import com.ifpr.leilao26.repository.RefreshTokenRepository;



@Service 
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration}")
    private Long expiration;

    @Autowired 
    private RefreshTokenRepository repo;

    public RefreshToken criar(Pessoa pessoa) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setPessoa(pessoa);
        refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken.setDataExpiracao(LocalDateTime.now().plus(Duration.ofMillis(expiration)));
        refreshToken.setRevogado(false);
        return repo.save(refreshToken);
    }

    
    public RefreshToken validar(String token) {
        RefreshToken refreshToken = repo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));
        if (refreshToken.isRevogado() || refreshToken.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expirado ou revogado");
        }
        return refreshToken;
    }

    public RefreshToken rotacionar(RefreshToken tokenAntigo) {
        tokenAntigo.setRevogado(true);
        repo.save(tokenAntigo);
        return criar(tokenAntigo.getPessoa());
    }

     public void revogar(String token) {
        repo.findByToken(token).ifPresent(rt -> {
            rt.setRevogado(true);
            repo.save(rt);
        });
    }
}
