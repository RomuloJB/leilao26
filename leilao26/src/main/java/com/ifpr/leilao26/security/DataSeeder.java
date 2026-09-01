package com.ifpr.leilao26.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.ifpr.leilao26.enums.TipoPerfil;
import com.ifpr.leilao26.model.Perfil;
import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.model.PessoaPerfil;
import com.ifpr.leilao26.repository.PerfilRepository;
import com.ifpr.leilao26.repository.PessoaPerfilRepository;
import com.ifpr.leilao26.repository.PessoaRepository;

import com.ifpr.leilao26.model.Categoria;
import com.ifpr.leilao26.repository.CategoriaRepository;


// Roda uma vez a cada subida da aplicação.
// Com spring.jpa.hibernate.ddl-auto=create o banco é recriado do zero a cada
// start, então isso garante que sempre existam os 3 perfis e o admin padrão.
// Se você mudar o ddl-auto para "update" no futuro, o comportamento
// continua correto: só cria o que ainda não existe.
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaPerfilRepository pessoaPerfilRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.email:admin@farmauction.com}")
    private String adminEmail;

    @Value("${admin.senha:admin}")
    private String adminSenha;

    @Override
    public void run(String... args) {
        Perfil perfilAdmin = garantirPerfil(TipoPerfil.ADMIN);
        garantirPerfil(TipoPerfil.COMPRADOR);
        garantirPerfil(TipoPerfil.VENDEDOR);
        garantirCategoria("Gado", "🐄", "Leilões de bovinos de corte e de leite direto da fazenda.");
        garantirCategoria("Cavalos", "🐎", "Leilões de cavalos de trabalho e esporte.");
        garantirCategoria("Ovelhas", "🐑", "Leilões de ovinos de corte e de lã.");

        if (pessoaRepository.findByUsername(adminUsername) == null) {
            Pessoa admin = new Pessoa();
            admin.setUsername(adminUsername);
            admin.setEmail(adminEmail);
            admin.setSenha(bCryptPasswordEncoder.encode(adminSenha));
            admin.setAtivo(true);
            Pessoa adminSalvo = pessoaRepository.save(admin);

            PessoaPerfil vinculo = new PessoaPerfil();
            vinculo.setPessoa(adminSalvo);
            vinculo.setPerfil(perfilAdmin);
            pessoaPerfilRepository.save(vinculo);

            System.out.println("Usuário admin padrão criado: " + adminUsername
                + " / senha definida em application.properties (admin.senha)");
        }
    }

    private Perfil garantirPerfil(TipoPerfil tipo) {
        return perfilRepository.findByTipo(tipo).orElseGet(() -> {
            Perfil novo = new Perfil();
            novo.setTipo(tipo);
            return perfilRepository.save(novo);
        });
    }

    private Categoria garantirCategoria(String nome, String icone, String observacao) {
        Categoria existente = categoriaRepository.findByNome(nome);
        if (existente != null) return existente;

        Categoria nova = new Categoria();
        nova.setNome(nome);
        nova.setIcone(icone);
        nova.setObservacao(observacao);
        return categoriaRepository.save(nova);
    }
}