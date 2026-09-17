package com.ifpr.leilao26.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import com.ifpr.leilao26.enums.StatusLeilao;
import com.ifpr.leilao26.model.Lance;
import com.ifpr.leilao26.model.Leilao;

import lombok.Data;

@Data
public class LeilaoResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String descricaoDetalhada;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private StatusLeilao status;
    private String observacao;
    private Float valorIncremento;
    private Float lanceMinimo;
    private Long categoriaId;
    private String categoriaNome;
    private Long vendedorId;
    private String vendedorUsername;
    private List<ImagemResponseDTO> imagens;
    // null enquanto o leilão não tiver nenhum lance
    private Float maiorLance;
    private int totalLances;

    public static LeilaoResponseDTO from(Leilao leilao) {
        LeilaoResponseDTO dto = new LeilaoResponseDTO();
        dto.id = leilao.getId();
        dto.titulo = leilao.getTitulo();
        dto.descricao = leilao.getDescricao();
        dto.descricaoDetalhada = leilao.getDescricaoDetalhada();
        dto.dataHoraInicio = leilao.getDataHoraInicio();
        dto.dataHoraFim = leilao.getDataHoraFim();
        dto.status = leilao.getStatus();
        dto.observacao = leilao.getObservacao();
        dto.valorIncremento = leilao.getValorIncremento();
        dto.lanceMinimo = leilao.getLanceMinimo();

        if (leilao.getCategoria() != null) {
            dto.categoriaId = leilao.getCategoria().getId();
            dto.categoriaNome = leilao.getCategoria().getNome();
        }
        if (leilao.getVendedor() != null) {
            dto.vendedorId = leilao.getVendedor().getId();
            dto.vendedorUsername = leilao.getVendedor().getUsername();
        }
        if (leilao.getImagens() != null) {
            dto.imagens = leilao.getImagens().stream().map(ImagemResponseDTO::from).toList();
        }
        if (leilao.getLances() != null) {
            dto.totalLances = leilao.getLances().size();
            dto.maiorLance = leilao.getLances().stream()
                .map(Lance::getValorLance)
                .filter(Objects::nonNull)
                .max(Float::compare)
                .orElse(null);
        }
        return dto;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getDescricaoDetalhada() { return descricaoDetalhada; }
    public LocalDateTime getDataHoraInicio() { return dataHoraInicio; }
    public LocalDateTime getDataHoraFim() { return dataHoraFim; }
    public StatusLeilao getStatus() { return status; }
    public String getObservacao() { return observacao; }
    public Float getValorIncremento() { return valorIncremento; }
    public Float getLanceMinimo() { return lanceMinimo; }
    public Long getCategoriaId() { return categoriaId; }
    public String getCategoriaNome() { return categoriaNome; }
    public Long getVendedorId() { return vendedorId; }
    public String getVendedorUsername() { return vendedorUsername; }
    public List<ImagemResponseDTO> getImagens() { return imagens; }
    public Float getMaiorLance() { return maiorLance; }
    public int getTotalLances() { return totalLances; }
}