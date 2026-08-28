package com.ifpr.leilao26.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ifpr.leilao26.model.Imagem;
import com.ifpr.leilao26.service.ImagemService;

@RestController
@CrossOrigin
@RequestMapping("/imagem")
public class ImagemController {
    @Autowired private ImagemService serv;

    @PostMapping(value = "/salvar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> salvarImagem(
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("leilaoId") Long leilaoId) {
        try {
            Imagem salva = serv.salvarImagem(arquivo, leilaoId);
            return ResponseEntity.status(HttpStatus.CREATED).body(salva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("message", "Erro ao processar o arquivo de imagem."));
        }
    }

    @GetMapping("/leilao/{leilaoId}")
    public List<Imagem> buscarPorLeilao(@PathVariable Long leilaoId) {
        return serv.buscarPorLeilao(leilaoId);
    }

    @GetMapping("/buscar")
    public List<Imagem> buscarTodos() {
        return serv.buscarTodos();
    }

    @GetMapping("/buscar/{id}")
    public Imagem buscarPorId(@PathVariable Long id) {
        return serv.buscarPorId(id);
    }

    @GetMapping("/{id}/arquivo")
    public ResponseEntity<byte[]> buscarArquivo(@PathVariable Long id) {
        Imagem imagem = serv.buscarPorId(id);

        if (imagem == null || imagem.getArquivo() == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(imagem.getTipoConteudo());
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok().contentType(mediaType).body(imagem.getArquivo());
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirImagem(@PathVariable Long id) {
        serv.excluirImagem(id);
        return ResponseEntity.noContent().build();
    }
}