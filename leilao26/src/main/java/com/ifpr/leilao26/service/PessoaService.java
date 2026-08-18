package com.ifpr.leilao26.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.repository.PessoaRepository;

@Service
public class PessoaService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PessoaService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    private PessoaRepository repo;

    public List<Pessoa> buscarTodos(){
        return repo.findAll();
    }

    public Pessoa buscarPessoaPorId(Long id){
        return repo.findById(id).orElse(null);
    }

    public Pessoa buscarPessoaPorUsuario(String username){
        return repo.findByUsername(username);
    }

    public Pessoa buscarPessoaPorEmail(String email){
        return repo.findByEmail(email);
    }

    public Pessoa criarPessoa(Pessoa pessoa) {
        pessoa.setSenha(bCryptPasswordEncoder.encode(pessoa.getSenha()));
        return repo.save(pessoa);
    }
    
    public Pessoa atualizarPessoa(Pessoa pessoa){
        return repo.save(pessoa);
    }

    public void excluirPessoa(Long id){
        repo.deleteById(id);
    }
}
