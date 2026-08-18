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

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.email:admin@farmauction.com}")
    private String adminEmail;

    @Value("${admin.senha:troque-esta-senha}")
    private String adminSenha;

    @Override
    public void run(String... args) {
        Perfil perfilAdmin = garantirPerfil(TipoPerfil.ADMIN);
        garantirPerfil(TipoPerfil.COMPRADOR);
        garantirPerfil(TipoPerfil.VENDEDOR);

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
}