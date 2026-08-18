package com.ifpr.leilao26.model;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name="pessoa")
public class Pessoa implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private String senha;

    private String codigoValidacao;

    private Date validadeCodigoValidacao;

    private Boolean ativo = true;
    @Lob
    private byte[] fotoPerfil;


    @OneToMany(mappedBy = "pessoa")
    private List<PessoaPerfil> perfis;

    @OneToMany(mappedBy = "criado_por")
    private List<Categoria> categorias;

    @OneToMany(mappedBy = "vendedor")
    private List<Leilao> leiloes;

    @OneToMany(mappedBy = "pessoa")
    private List<Lance> lances;

    @OneToMany(mappedBy = "autor")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "destinatario")
    private List<Feedback> feedbackRecebido;

    //metodos exigidos pela interface UserDetails (spring security
    // Implementar UserDetails diretamente na Pessoa evita ter que manter uma classe de usuário separada: o Spring Security passa a entender a Pessoa como "o usuário autenticável" nativamente.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (perfis == null) {
            return List.of();
        }
        return perfis.stream()
            .map(pessoaPerfil -> new SimpleGrantedAuthority("ROLE_" + pessoaPerfil.getPerfil().getTipo().name()))
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo != null && ativo;
    }
}
