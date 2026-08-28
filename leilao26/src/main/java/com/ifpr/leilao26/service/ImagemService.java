package com.ifpr.leilao26.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ifpr.leilao26.model.Imagem;
import com.ifpr.leilao26.model.Leilao;
import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.repository.ImagemRepository;
import com.ifpr.leilao26.repository.LeilaoRepository;
import com.ifpr.leilao26.repository.PessoaRepository;

@Service
public class ImagemService {
    @Autowired private ImagemRepository repo;
    @Autowired private LeilaoRepository leilaoRepo;
    @Autowired private PessoaRepository pessoaRepo;

    public Imagem salvarImagem(MultipartFile arquivo, Long leilaoId) throws IOException {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Selecione um arquivo de imagem.");
        }

        Leilao leilao = leilaoRepo.findById(leilaoId)
            .orElseThrow(() -> new IllegalArgumentException("Leilão não encontrado."));

        Imagem imagem = new Imagem();
        imagem.setNomeImagem(arquivo.getOriginalFilename());
        imagem.setTipoConteudo(arquivo.getContentType());
        imagem.setArquivo(arquivo.getBytes());
        imagem.setDataHoraCadastro(LocalDateTime.now());
        imagem.setLeilao(leilao);
        imagem.setPessoa(usuarioLogado());

        return repo.save(imagem);
    }

    public List<Imagem> buscarPorLeilao(Long leilaoId) {
        return repo.findByLeilaoId(leilaoId);
    }

    public List<Imagem> buscarTodos() {
        return repo.findAll();
    }

    public Imagem buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void excluirImagem(Long id) {
        repo.deleteById(id);
    }
    
    private Pessoa usuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        return pessoaRepo.findByUsername(auth.getName());
    }
}