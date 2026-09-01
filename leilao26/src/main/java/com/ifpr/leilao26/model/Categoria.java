package com.ifpr.leilao26.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name="categoria")
public class Categoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    private String observacao;
    private String icone;


    @ManyToOne
    private Pessoa criador;

    @OneToMany(mappedBy = "categoria")
    private List<Leilao> leiloes;

    @ManyToOne
    private Pessoa criado_por;
}
