package com.ifpr.leilao26.dto;

import java.time.LocalDateTime;
import com.ifpr.leilao26.model.Imagem;

public class ImagemResponseDTO {
    private Long id;
    private String url;
    private LocalDateTime dataHoraCadastro;

    public static ImagemResponseDTO from(Imagem imagem) {
        ImagemResponseDTO dto = new ImagemResponseDTO();
        dto.id = imagem.getId();
        dto.url = "/imagem/arquivo/" + imagem.getId();
        dto.dataHoraCadastro = imagem.getDataHoraCadastro();
        return dto;
    }

    public Long getId() { return id; }
    public String getUrl() { return url; }
    public LocalDateTime getDataHoraCadastro() { return dataHoraCadastro; }
}