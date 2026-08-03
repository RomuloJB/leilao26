package com.ifpr.leilao26.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.repository.PessoaRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
    private final PessoaRepository pessoaRepository;

    public MyUserDetailsService(PessoaRepository pessoaRepository){
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Pessoa pessoa = pessoaRepository.findByUsername(username);

        if(pessoa == null) {
            throw new UsernameNotFoundException("O usuário desta pessoa não foi encontrado!");
        }

        return new PessoaPrincipal(pessoa);
    }
}
