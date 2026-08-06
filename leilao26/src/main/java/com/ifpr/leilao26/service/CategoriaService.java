package com.ifpr.leilao26.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.model.Categoria;
import com.ifpr.leilao26.repository.CategoriaRepository;

@Service
public class CategoriaService {
    @Autowired private CategoriaRepository repo;

    public Categoria criarCategoria(Categoria categoria){
        return repo.save(categoria);
    }

    public Categoria atualizarCategoria(Categoria categoria){
        return repo.save(categoria);
    }

    public List<Categoria> buscarTodos(){
        return repo.findAll();
    }

    public Categoria buscarPorId(Long id){
        return repo.findById(id).orElse(null);
    }

    public Categoria buscarPorNome(String nome){
        return repo.findByNome(nome);
    }

    public void excluirCategoria(Long id){
        repo.deleteById(id);
    }
}
