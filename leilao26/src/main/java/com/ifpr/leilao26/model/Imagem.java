package com.ifpr.leilao26.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="imagem")
public class Imagem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHoraCadastro;
    private String nomeImagem;

    @ManyToOne
    private Leilao leilao;

    @ManyToOne
    private Pessoa pessoa;
}
