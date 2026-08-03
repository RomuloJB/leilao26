package com.ifpr.leilao26.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name="categoria")
public class Categoria {
    @NotBlank
    private String nome;
    private String observacao;
}
