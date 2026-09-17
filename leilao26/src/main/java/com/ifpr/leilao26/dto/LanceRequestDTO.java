package com.ifpr.leilao26.dto;

// Corpo do POST /lance/registrar. Quem dá o lance é sempre o usuário logado (vem do token),
// por isso não existe campo pessoaId aqui.
public class LanceRequestDTO {
    private Long leilaoId;
    private Float valorLance;

    public Long getLeilaoId() { return leilaoId; }
    public void setLeilaoId(Long leilaoId) { this.leilaoId = leilaoId; }

    public Float getValorLance() { return valorLance; }
    public void setValorLance(Float valorLance) { this.valorLance = valorLance; }
}
