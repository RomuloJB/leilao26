package com.ifpr.leilao26.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Autowired
    private EmailService emailService;

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

    public void gerarCodigoRecuperacao(String email) {
        Pessoa pessoa = repo.findByEmail(email);
        if (pessoa == null) {
            return; // resposta genérica no controller não revela se o e-mail existe
        }

        String codigo = String.valueOf(100000 + new Random().nextInt(900000));
        pessoa.setCodigoValidacao(codigo);
        pessoa.setValidadeCodigoValidacao(LocalDateTime.now().plusMinutes(15));
        repo.save(pessoa);

        emailService.enviarEmail(
            pessoa.getEmail(),
            "Recuperação de senha - Leilão26",
            "<p>Seu código de recuperação é: <b>" + codigo + "</b></p><p>Válido por 15 minutos.</p>"
        );
    }

    public void alterarSenhaComCodigo(String email, String codigo, String novaSenha) {
        Pessoa pessoa = repo.findByEmail(email);

        boolean invalido = pessoa == null
            || pessoa.getCodigoValidacao() == null
            || !pessoa.getCodigoValidacao().equals(codigo)
            || pessoa.getValidadeCodigoValidacao().isBefore(LocalDateTime.now());

        if (invalido) {
            throw new IllegalArgumentException("Código inválido ou expirado.");
        }

        pessoa.setSenha(bCryptPasswordEncoder.encode(novaSenha));
        pessoa.setCodigoValidacao(null);
        pessoa.setValidadeCodigoValidacao(null);
        repo.save(pessoa);
    }
}