package com.ifpr.leilao26.model;

import java.time.LocalDateTime;
import java.util.List;

import com.ifpr.leilao26.enums.StatusLeilao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name="leilao")
public class Leilao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;
    
    @NotBlank
    private String descricao;

    private String descricaoDetalhada;

    @NotNull
    private LocalDateTime dataHoraInicio;

    @NotNull
    private LocalDateTime dataHoraFim;

    private StatusLeilao status;

    private String observacao;

    @NotNull
    private Float valorIncremento;

    @NotNull
    private Float lanceMinimo;

    @ManyToOne
    private Pessoa vendedor;

    @OneToOne
    private Categoria categoria;

    @OneToMany(mappedBy = "leilao")
    private List<Imagem> imagens;

    @OneToMany(mappedBy = "leilao")
    private List<Lance> lances;

    @ManyToOne
    private Pagamento pagamento;
}
