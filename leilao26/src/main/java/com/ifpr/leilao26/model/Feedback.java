package com.ifpr.leilao26.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name="feedback")
public class Feedback {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentario;

    @NotBlank 
    @Min(value=1, message="A nota deve ser maior que 1")
    @Max(value=5, message="A nota deve ser menor que 5")
    private Integer nota;

    private LocalDateTime dataHora;

    @ManyToOne
    private Pessoa autor;

    @ManyToOne
    private Pessoa destinatario;

    @ManyToOne
    private Pessoa pessoa;
}
