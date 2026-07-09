package com.ifpr.leilao26.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.repository.PessoaRepository;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository repo;
}
