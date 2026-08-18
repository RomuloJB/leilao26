package com.ifpr.leilao26.dto;

import com.ifpr.leilao26.enums.TipoPerfil;

public class CadastroRequest {
    private String username;
    private String email;
    private String senha;

    private TipoPerfil tipoPerfil;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public TipoPerfil getTipoPerfil() { return tipoPerfil; }
    public void setTipoPerfil(TipoPerfil tipoPerfil) { this.tipoPerfil = tipoPerfil; }
}