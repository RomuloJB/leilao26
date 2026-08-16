package com.ifpr.leilao26.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifpr.leilao26.model.PessoaPerfil;

public interface PessoaPerfilRepository extends JpaRepository<PessoaPerfil, Long> {
    List<PessoaPerfil> findByPessoaId(Long pessoaId);
    List<PessoaPerfil> findByPerfilId(Long perfilId);
    boolean existsByPessoaIdAndPerfilId(Long pessoaId, Long perfilId);
}
