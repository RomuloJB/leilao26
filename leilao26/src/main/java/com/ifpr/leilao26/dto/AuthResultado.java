package com.ifpr.leilao26.dto;

// Resultado interno do AuthService: "dados" vai no corpo da resposta para o React,
// e "refreshToken" vai num cookie HttpOnly (o JavaScript nunca vê esse valor).
public record AuthResultado(AuthResponse dados, String refreshToken) {}
