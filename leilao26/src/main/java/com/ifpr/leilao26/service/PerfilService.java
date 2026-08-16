package com.ifpr.leilao26.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.model.Perfil;
import com.ifpr.leilao26.repository.PerfilRepository;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository repo;

    public List<Perfil> buscarTodos() {
        return repo.findAll();
    }

    public Perfil buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Perfil criarPerfil(Perfil perfil) {
        return repo.save(perfil);
    }

    public Perfil atualizarPerfil(Perfil perfil) {
        return repo.save(perfil);
    }

    public void excluirPerfil(Long id) {
        repo.deleteById(id);
    }
}
