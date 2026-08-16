package com.ifpr.leilao26.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.model.Perfil;
import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.model.PessoaPerfil;
import com.ifpr.leilao26.repository.PerfilRepository;
import com.ifpr.leilao26.repository.PessoaPerfilRepository;
import com.ifpr.leilao26.repository.PessoaRepository;

@Service
public class PessoaPerfilService {

    @Autowired
    private PessoaPerfilRepository repo;

    @Autowired
    private PessoaRepository pessoaRepo;

    @Autowired
    private PerfilRepository perfilRepo;

    public List<PessoaPerfil> buscarPorPessoa(Long pessoaId) {
        return repo.findByPessoaId(pessoaId);
    }

    public List<PessoaPerfil> buscarPorPerfil(Long perfilId) {
        return repo.findByPerfilId(perfilId);
    }

    // Atribui um perfil (ex.: ADMIN, VENDEDOR) a uma pessoa.
    // É essa combinação pessoa+perfil que o Spring Security vai ler
    // para montar as "authorities" (roles) de quem está logado.
    public PessoaPerfil atribuirPerfil(Long pessoaId, Long perfilId) {
        if (repo.existsByPessoaIdAndPerfilId(pessoaId, perfilId)) {
            throw new IllegalStateException("Este perfil já está atribuído a esta pessoa.");
        }

        Pessoa pessoa = pessoaRepo.findById(pessoaId)
            .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada."));
        Perfil perfil = perfilRepo.findById(perfilId)
            .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado."));

        PessoaPerfil pessoaPerfil = new PessoaPerfil();
        pessoaPerfil.setPessoa(pessoa);
        pessoaPerfil.setPerfil(perfil);
        return repo.save(pessoaPerfil);
    }

    public void removerPerfil(Long id) {
        repo.deleteById(id);
    }
}
