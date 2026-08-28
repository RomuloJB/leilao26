package com.ifpr.leilao26.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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

    private String tipoConteudo;

    @Lob
    @JsonIgnore
    private byte[] arquivo;

    @JsonIgnore
    @ManyToOne
    private Leilao leilao;

    @JsonIgnore
    @ManyToOne
    private Pessoa pessoa;
}
