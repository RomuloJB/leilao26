package com.ifpr.leilao26.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpr.leilao26.dto.AuthResponse;
import com.ifpr.leilao26.dto.CadastroRequest;
import com.ifpr.leilao26.dto.LoginRequest;
import com.ifpr.leilao26.enums.TipoPerfil;
import com.ifpr.leilao26.model.Perfil;
import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.model.PessoaPerfil;
import com.ifpr.leilao26.repository.PerfilRepository;
import com.ifpr.leilao26.repository.PessoaPerfilRepository;
import com.ifpr.leilao26.repository.PessoaRepository;
import com.ifpr.leilao26.service.JwtService;
import com.ifpr.leilao26.service.PessoaService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PessoaPerfilRepository pessoaPerfilRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getSenha())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Usuário ou senha inválidos."));
        }

        Pessoa pessoa = pessoaRepository.findByUsername(request.getUsername());
        String token = jwtService.gerarToken(pessoa);

        return ResponseEntity.ok(new AuthResponse(token, pessoa.getId(), pessoa.getUsername()));
    }

    // Cadastro público: cria a Pessoa e já vincula o Perfil escolhido (COMPRADOR
    // ou VENDEDOR)
    @PostMapping("/registrar")
    @Transactional
    public ResponseEntity<?> registrar(@RequestBody CadastroRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()
            || request.getEmail() == null || request.getEmail().isBlank()
            || request.getSenha() == null || request.getSenha().isBlank()) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Preencha usuário, e-mail e senha."));
        }

        if (request.getTipoPerfil() != TipoPerfil.COMPRADOR && request.getTipoPerfil() != TipoPerfil.VENDEDOR) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Selecione se deseja se cadastrar como comprador ou vendedor."));
        }

        if (pessoaRepository.findByUsername(request.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "Este nome de usuário já está em uso."));
        }

        if (pessoaRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "Este e-mail já está cadastrado."));
        }

        Perfil perfil = perfilRepository.findByTipo(request.getTipoPerfil())
            .orElseThrow(() -> new IllegalStateException(
                "Perfil " + request.getTipoPerfil() + " não está cadastrado no sistema."));

        Pessoa pessoa = new Pessoa();
        pessoa.setUsername(request.getUsername());
        pessoa.setEmail(request.getEmail());
        pessoa.setSenha(request.getSenha());
        // pessoaService.criarPessoa já cuida do encode (BCrypt) da senha.
        Pessoa pessoaSalva = pessoaService.criarPessoa(pessoa);

        PessoaPerfil vinculo = new PessoaPerfil();
        vinculo.setPessoa(pessoaSalva);
        vinculo.setPerfil(perfil);
        pessoaPerfilRepository.save(vinculo);

        String token = jwtService.gerarToken(pessoaSalva);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new AuthResponse(token, pessoaSalva.getId(), pessoaSalva.getUsername()));
    }
}