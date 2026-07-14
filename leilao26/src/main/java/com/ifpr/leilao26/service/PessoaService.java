package com.ifpr.leilao26.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.repository.PessoaRepository;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository repo;

    public List<Pessoa> findAll(){
        return repo.findAll();
    }
    
}
