package com.ifpr.leilao26.dto;

import java.time.LocalDateTime;

import com.ifpr.leilao26.model.Lance;

// Nunca devolver a entidade Lance direto: ela carrega Pessoa (com hash de senha) e Leilao inteiros.
public class LanceResponseDTO {
    private Long id;
    private Float valorLance;
    private LocalDateTime dataHora;
    private Long leilaoId;
    private Long pessoaId;
    private String pessoaUsername;

    public static LanceResponseDTO from(Lance lance) {
        LanceResponseDTO dto = new LanceResponseDTO();
        dto.id = lance.getId();
        dto.valorLance = lance.getValorLance();
        dto.dataHora = lance.getDataHora();

        if (lance.getLeilao() != null) {
            dto.leilaoId = lance.getLeilao().getId();
        }
        if (lance.getPessoa() != null) {
            dto.pessoaId = lance.getPessoa().getId();
            dto.pessoaUsername = lance.getPessoa().getUsername();
        }
        return dto;
    }

    public Long getId() { return id; }
    public Float getValorLance() { return valorLance; }
    public LocalDateTime getDataHora() { return dataHora; }
    public Long getLeilaoId() { return leilaoId; }
    public Long getPessoaId() { return pessoaId; }
    public String getPessoaUsername() { return pessoaUsername; }
}
