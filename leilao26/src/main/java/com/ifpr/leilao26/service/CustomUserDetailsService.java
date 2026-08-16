package com.ifpr.leilao26.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.repository.PessoaRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PessoaRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Assume que PessoaRepository tem: Pessoa findByUsername(String username);
        // Ajuste esse método se a assinatura no seu repositório for diferente.
        Pessoa pessoa = repo.findByUsername(username);
        if (pessoa == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        return pessoa;
    }
}
