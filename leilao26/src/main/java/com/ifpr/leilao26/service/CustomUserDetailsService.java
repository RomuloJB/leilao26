package com.ifpr.leilao26.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.repository.PessoaRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PessoaRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // findByUsernameComPerfis já vem com JOIN FETCH dos perfis/perfil.
        // Isso evita LazyInitializationException quando Pessoa.getAuthorities()
        // percorre a lista de perfis fora de uma transação aberta.
        return repo.findByUsernameComPerfis(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}