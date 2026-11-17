package com.ifpr.leilao26.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ifpr.leilao26.enums.TipoPerfil;
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

    // Chamado quando a pessoa cria um leilão: o perfil COMPRADOR (dado no cadastro)
    // muda para VENDEDOR. Os dois perfis têm os mesmos direitos (comprar e vender);
    // a troca só registra que a pessoa já anunciou algo.
    // Quem já é VENDEDOR ou não tem COMPRADOR (ex.: ADMIN) fica como está.
    @Transactional
    public void tornarVendedor(Long pessoaId) {
        List<PessoaPerfil> vinculos = repo.findByPessoaId(pessoaId);

        boolean jaEhVendedor = vinculos.stream()
            .anyMatch(v -> v.getPerfil().getTipo() == TipoPerfil.VENDEDOR);
        if (jaEhVendedor) {
            return;
        }

        vinculos.stream()
            .filter(v -> v.getPerfil().getTipo() == TipoPerfil.COMPRADOR)
            .findFirst()
            .ifPresent(vinculoComprador -> {
                Perfil vendedor = perfilRepo.findByTipo(TipoPerfil.VENDEDOR)
                    .orElseThrow(() -> new IllegalStateException(
                        "Perfil VENDEDOR não está cadastrado no sistema."));
                vinculoComprador.setPerfil(vendedor);
                repo.save(vinculoComprador);
            });
    }
}
