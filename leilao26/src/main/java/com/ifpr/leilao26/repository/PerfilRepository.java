package com.ifpr.leilao26.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifpr.leilao26.enums.TipoPerfil;
import com.ifpr.leilao26.model.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByTipo(TipoPerfil tipo);
}
