package com.ifpr.leilao26.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ifpr.leilao26.enums.StatusLeilao;

import jakarta.persistence.CascadeType;
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

    @JsonIgnore
    @ManyToOne
    private Pessoa vendedor;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private Categoria categoria;

    // cascade REMOVE: ao excluir o leilão, as imagens e os lances dele vão junto
    // (senão a FK imagem.leilao_id / lance.leilao_id barra a exclusão com erro 500).
    @JsonIgnore
    @OneToMany(mappedBy = "leilao", cascade = CascadeType.REMOVE)
    private List<Imagem> imagens;

    @JsonIgnore
    @OneToMany(mappedBy = "leilao", cascade = CascadeType.REMOVE)
    private List<Lance> lances;

    @JsonIgnore
    @ManyToOne
    private Pagamento pagamento;
}
