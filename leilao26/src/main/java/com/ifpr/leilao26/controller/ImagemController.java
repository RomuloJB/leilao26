package com.ifpr.leilao26.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpr.leilao26.model.Imagem;
import com.ifpr.leilao26.service.ImagemService;

@RestController
@CrossOrigin
@RequestMapping("/imagem")
public class ImagemController {
    @Autowired private ImagemService serv;

    @PostMapping("/salvar")
    public ResponseEntity<Imagem> salvarImagem(@RequestBody() Imagem imagem){
        Imagem salvarImagem = serv.salvarImagem(imagem);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(salvarImagem);
    }

    @PutMapping("/atualizar/{id}")
    public Imagem atualizarImagem(@RequestBody() Imagem imagem, @PathVariable("id") Long id) {
        return serv.atualizarImagem(imagem);
    }   

    @GetMapping("/buscar")
    public List<Imagem> buscarTodos(){
        return serv.buscarTodos();
    }

    @GetMapping("/buscar/{id}")
    public Imagem buscarPorId(@PathVariable("id") Long id){
        return serv.buscarPorId(id);
    }

    @DeleteMapping("/excluir/{id}")
    public void excluirImagem(@PathVariable("id") Long id){
        serv.excluirImagem(id);
    }
}