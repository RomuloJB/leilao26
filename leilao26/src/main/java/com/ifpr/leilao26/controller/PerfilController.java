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

import com.ifpr.leilao26.model.Perfil;
import com.ifpr.leilao26.service.PerfilService;

@RestController
@RequestMapping("/perfil")
@CrossOrigin
public class PerfilController {

    @Autowired
    private PerfilService serv;

    @GetMapping("/buscar")
    public List<Perfil> buscarTodos() {
        return serv.buscarTodos();
    }

    @GetMapping("/buscar/{id}")
    public Perfil buscarPorId(@PathVariable Long id) {
        return serv.buscarPorId(id);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Perfil> criarPerfil(@RequestBody Perfil perfil) {
        Perfil novo = serv.criarPerfil(perfil);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @PutMapping("/atualizar/{id}")
    public Perfil atualizarPerfil(@RequestBody Perfil perfil, @PathVariable Long id) {
        perfil.setId(id);
        return serv.atualizarPerfil(perfil);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirPerfil(@PathVariable Long id) {
        serv.excluirPerfil(id);
        return ResponseEntity.noContent().build();
    }
}
