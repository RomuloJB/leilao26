package com.ifpr.leilao26.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.model.Imagem;
import com.ifpr.leilao26.repository.ImagemRepository;

@Service
public class ImagemService {
    @Autowired private ImagemRepository repo;

    public Imagem salvarImagem(Imagem imagem){
        return repo.save(imagem);
    }

    public Imagem atualizarImagem(Imagem imagem){
        return repo.save(imagem);
    }

    public List<Imagem> buscarTodos(){
        return repo.findAll();
    }

    public Imagem buscarPorId(Long id){
        return repo.findById(id).orElse(null);
    }

    public void excluirImagem(Long id){
        repo.deleteById(id);
    }
}
