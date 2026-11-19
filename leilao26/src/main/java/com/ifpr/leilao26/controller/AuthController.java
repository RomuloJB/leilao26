package com.ifpr.leilao26.controller;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpr.leilao26.dto.AuthResponse;
import com.ifpr.leilao26.dto.AuthResultado;
import com.ifpr.leilao26.dto.CadastroRequest;
import com.ifpr.leilao26.dto.LoginRequest;
import com.ifpr.leilao26.enums.TipoPerfil;
import com.ifpr.leilao26.model.Perfil;
import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.model.PessoaPerfil;
import com.ifpr.leilao26.repository.PerfilRepository;
import com.ifpr.leilao26.repository.PessoaPerfilRepository;
import com.ifpr.leilao26.repository.PessoaRepository;
import com.ifpr.leilao26.service.AuthService;
import com.ifpr.leilao26.service.PessoaService;

import com.ifpr.leilao26.dto.AlterarSenhaRequest;
import com.ifpr.leilao26.dto.RecuperarSenhaRequest;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private static final String COOKIE_REFRESH_TOKEN = "refreshToken";

    @Autowired
    private AuthService authService;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PessoaPerfilRepository pessoaPerfilRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResultado resultado;
        try {
            resultado = authService.autenticar(request);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Usuário ou senha inválidos."));
        }

        adicionarCookieRefreshToken(response, resultado.refreshToken());
        return ResponseEntity.ok(resultado.dados());
    }

    // O navegador envia o cookie refreshToken sozinho (o path dele é /auth).
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(name = COOKIE_REFRESH_TOKEN, required = false) String refreshToken,
            HttpServletResponse response) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            AuthResultado resultado = authService.renovar(refreshToken);
            adicionarCookieRefreshToken(response, resultado.refreshToken());
            return ResponseEntity.ok(resultado.dados());
        } catch (RuntimeException e) {
            removerCookieRefreshToken(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Revoga o refresh token no banco e apaga o cookie.
    // O access token continua válido até expirar (JWT não pode ser cancelado),
    // por isso o front também precisa descartá-lo.
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = COOKIE_REFRESH_TOKEN, required = false) String refreshToken,
            HttpServletResponse response) {
        if (refreshToken != null) {
            authService.logout(refreshToken);
        }
        removerCookieRefreshToken(response);
        return ResponseEntity.noContent().build();
    }

    // Devolve os dados e perfis atuais de quem está logado.
    // O front chama depois de criar um leilão, porque o perfil pode ter mudado
    // de COMPRADOR para VENDEDOR (ver PessoaPerfilService.tornarVendedor).
    @GetMapping("/me")
    public ResponseEntity<?> usuarioLogado(@AuthenticationPrincipal Pessoa pessoaLogada) {
        List<String> roles = pessoaLogada.getAuthorities().stream()
            .map(a -> a.getAuthority().replace("ROLE_", ""))
            .toList();
        return ResponseEntity.ok(Map.of(
            "id", pessoaLogada.getId(),
            "username", pessoaLogada.getUsername(),
            "roles", roles));
    }

    // Cadastro público: cria a Pessoa e atribui automaticamente o perfil COMPRADOR.
    // O usuário não escolhe o perfil: ele passa a VENDEDOR sozinho quando cria o
    // primeiro leilão. COMPRADOR e VENDEDOR têm os mesmos direitos (comprar e vender).
    @PostMapping("/registrar")
    @Transactional
    public ResponseEntity<?> registrar(@RequestBody CadastroRequest request, HttpServletResponse response) {
        if (request.getUsername() == null || request.getUsername().isBlank()
            || request.getEmail() == null || request.getEmail().isBlank()
            || request.getSenha() == null || request.getSenha().isBlank()) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Preencha usuário, e-mail e senha."));
        }

        if (pessoaRepository.findByUsername(request.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "Este nome de usuário já está em uso."));
        }

        if (pessoaRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "Este e-mail já está cadastrado."));
        }

        Perfil perfil = perfilRepository.findByTipo(TipoPerfil.COMPRADOR)
            .orElseThrow(() -> new IllegalStateException(
                "Perfil COMPRADOR não está cadastrado no sistema."));

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
        // a pessoa recém-salva ainda não tem a lista de perfis carregada;
        // preenche para o AuthService montar as roles da resposta
        pessoaSalva.setPerfis(List.of(vinculo));

        AuthResultado resultado = authService.iniciarSessao(pessoaSalva);
        adicionarCookieRefreshToken(response, resultado.refreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado.dados());
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<Map<String, String>> recuperarSenha(@RequestBody RecuperarSenhaRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Informe o e-mail."));
        }

        try {
            pessoaService.gerarCodigoRecuperacao(request.getEmail());
        } catch (MailException e) {
            // falha no SMTP (credenciais, rede...): registra no log e avisa o usuário sem expor detalhes
            log.error("Falha ao enviar e-mail de recuperação de senha", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("message",
                "Não foi possível enviar o e-mail agora. Tente novamente mais tarde."));
        }

        return ResponseEntity.ok(Map.of("message",
            "Se o e-mail informado estiver cadastrado, um código de recuperação foi enviado."));
    }

    @PostMapping("/alterar-senha")
    public ResponseEntity<Map<String, String>> alterarSenha(@RequestBody AlterarSenhaRequest request) {
        try {
            pessoaService.alterarSenhaComCodigo(request.getEmail(), request.getCodigo(), request.getNovaSenha());
            return ResponseEntity.ok(Map.of("message", "Senha alterada com sucesso."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    private void adicionarCookieRefreshToken(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_REFRESH_TOKEN, refreshToken)
            .httpOnly(true)   // o JavaScript da página não consegue ler o cookie
            .secure(false)    // true em produção, com HTTPS
            .sameSite("Lax")  // não é enviado em POST vindo de outros sites (defesa contra CSRF)
            .path("/auth")    // só vai para URLs que começam com /auth
            .maxAge(Duration.ofMillis(refreshExpiration))
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void removerCookieRefreshToken(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_REFRESH_TOKEN, "")
            .httpOnly(true).secure(false).sameSite("Lax")
            .path("/auth")
            .maxAge(0) // maxAge 0 = o navegador apaga o cookie
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}