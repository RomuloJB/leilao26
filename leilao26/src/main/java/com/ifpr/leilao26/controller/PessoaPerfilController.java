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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ifpr.leilao26.model.PessoaPerfil;
import com.ifpr.leilao26.service.PessoaPerfilService;

@RestController
@RequestMapping("/pessoa-perfil")
@CrossOrigin
public class PessoaPerfilController {

    @Autowired
    private PessoaPerfilService serv;

    @GetMapping("/pessoa/{pessoaId}")
    public List<PessoaPerfil> buscarPorPessoa(@PathVariable Long pessoaId) {
        return serv.buscarPorPessoa(pessoaId);
    }

    @GetMapping("/perfil/{perfilId}")
    public List<PessoaPerfil> buscarPorPerfil(@PathVariable Long perfilId) {
        return serv.buscarPorPerfil(perfilId);
    }

    @PostMapping("/atribuir")
    public ResponseEntity<?> atribuirPerfil(@RequestParam Long pessoaId, @RequestParam Long perfilId) {
        try {
            PessoaPerfil novo = serv.atribuirPerfil(pessoaId, perfilId);
            return ResponseEntity.status(HttpStatus.CREATED).body(novo);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> removerPerfil(@PathVariable Long id) {
        serv.removerPerfil(id);
        return ResponseEntity.noContent().build();
    }
}
