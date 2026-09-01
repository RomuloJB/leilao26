package com.ifpr.leilao26.dto;

import com.ifpr.leilao26.model.Categoria;

import lombok.Data;

@Data
public class CategoriaResponseDTO {
    private Long id;
    private String nome;
    private String observacao;
    private String icone;

    public static CategoriaResponseDTO from(Categoria c) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.id = c.getId();
        dto.nome = c.getNome();
        dto.observacao = c.getObservacao();
        dto.icone = c.getIcone();
        return dto;
    }
    // getters
}