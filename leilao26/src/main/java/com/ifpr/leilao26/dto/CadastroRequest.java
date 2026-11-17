package com.ifpr.leilao26.dto;

// O perfil não vem mais no cadastro: todo novo usuário entra como COMPRADOR
// (atribuído no AuthController.registrar).
public class CadastroRequest {
    private String username;
    private String email;
    private String senha;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
