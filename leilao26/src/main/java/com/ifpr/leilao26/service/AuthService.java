package com.ifpr.leilao26.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.dto.AuthResponse;
import com.ifpr.leilao26.dto.AuthResultado;
import com.ifpr.leilao26.dto.LoginRequest;
import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.model.RefreshToken;
import com.ifpr.leilao26.repository.PessoaRepository;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private PessoaRepository pessoaRepository;

    // authenticate chama o loadUserByUsername do CustomUserDetailsService e compara a senha com BCrypt.
    // Se a senha estiver errada (ou a conta desativada), lança AuthenticationException,
    // que o AuthController transforma em 401.
    public AuthResultado autenticar(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getSenha()));
        Pessoa pessoa = buscarComPerfis(request.getUsername());
        return iniciarSessao(pessoa);
    }

    // Cria o refresh token e o access token de uma pessoa já autenticada.
    // Usado no login e no cadastro (que já deixa o usuário logado).
    public AuthResultado iniciarSessao(Pessoa pessoa) {
        RefreshToken refreshToken = refreshTokenService.criar(pessoa);
        return montarResultado(pessoa, refreshToken);
    }

    // Não pede senha: confia no refresh token válido, rotaciona (revoga o antigo e cria um novo)
    // e gera um access token novo.
    public AuthResultado renovar(String refreshTokenRecebido) {
        RefreshToken valido = refreshTokenService.validar(refreshTokenRecebido);
        // busca de novo com os perfis: eles podem ter mudado (ex.: COMPRADOR virou VENDEDOR)
        Pessoa pessoa = buscarComPerfis(valido.getPessoa().getUsername());
        if (!pessoa.isEnabled()) {
            refreshTokenService.revogar(refreshTokenRecebido);
            throw new RuntimeException("Usuário desativado");
        }
        RefreshToken novo = refreshTokenService.rotacionar(valido);
        return montarResultado(pessoa, novo);
    }

    public void logout(String refreshTokenRecebido) {
        refreshTokenService.revogar(refreshTokenRecebido);
    }

    private Pessoa buscarComPerfis(String username) {
        return pessoaRepository.findByUsernameComPerfis(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    private AuthResultado montarResultado(Pessoa pessoa, RefreshToken refreshToken) {
        List<String> roles = pessoa.getAuthorities().stream()
            .map(a -> a.getAuthority().replace("ROLE_", ""))
            .toList();
        AuthResponse dados = new AuthResponse(
            jwtService.gerarToken(pessoa), pessoa.getId(), pessoa.getUsername(), roles);
        return new AuthResultado(dados, refreshToken.getToken());
    }
}
