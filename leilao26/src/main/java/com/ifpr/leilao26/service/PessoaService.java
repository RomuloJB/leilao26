package com.ifpr.leilao26.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.repository.PessoaRepository;

@Service
public class PessoaService {
    private static final int MINUTOS_VALIDADE_CODIGO = 15;

    // SecureRandom: o código dá acesso à conta, não pode ser previsível
    private final SecureRandom random = new SecureRandom();

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

        String codigo = String.valueOf(100000 + random.nextInt(900000));
        pessoa.setCodigoValidacao(codigo);
        pessoa.setValidadeCodigoValidacao(LocalDateTime.now().plusMinutes(MINUTOS_VALIDADE_CODIGO));
        repo.save(pessoa);

        emailService.enviarEmailRecuperacaoSenha(pessoa.getEmail(), pessoa.getUsername(), codigo, MINUTOS_VALIDADE_CODIGO);
    }

    public void alterarSenhaComCodigo(String email, String codigo, String novaSenha) {
        if (novaSenha == null || novaSenha.isBlank()) {
            throw new IllegalArgumentException("Informe a nova senha.");
        }

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

    // Troca de senha com o usuário já logado: exige a senha atual antes de gravar a nova.
    public void trocarSenha(Long idPessoa, String senhaAtual, String novaSenha) {
        if (senhaAtual == null || senhaAtual.isBlank() || novaSenha == null || novaSenha.isBlank()) {
            throw new IllegalArgumentException("Informe a senha atual e a nova senha.");
        }

        // busca de novo no banco para comparar com o hash mais recente da senha
        Pessoa pessoa = repo.findById(idPessoa)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (!bCryptPasswordEncoder.matches(senhaAtual, pessoa.getSenha())) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }

        if (bCryptPasswordEncoder.matches(novaSenha, pessoa.getSenha())) {
            throw new IllegalArgumentException("A nova senha deve ser diferente da atual.");
        }

        pessoa.setSenha(bCryptPasswordEncoder.encode(novaSenha));
        repo.save(pessoa);
    }
}