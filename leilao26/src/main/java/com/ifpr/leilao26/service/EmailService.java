package com.ifpr.leilao26.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.remetente}")
    private String remetente;

    private final RestClient restClient = RestClient.create("https://api.resend.com");

    public void enviarEmail(String destinatario, String assunto, String htmlCorpo) {
        restClient.post()
            .uri("/emails")
            .header("Authorization", "Bearer " + apiKey)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of(
                "from", remetente,
                "to", destinatario,
                "subject", assunto,
                "html", htmlCorpo
            ))
            .retrieve()
            .toBodilessEntity();
    }
}