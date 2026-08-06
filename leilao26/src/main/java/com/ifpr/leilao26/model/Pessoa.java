package com.ifpr.leilao26.model;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name="pessoa")
public class Pessoa{
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

    private Boolean ativo;
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
    
}
