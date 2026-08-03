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

import com.ifpr.leilao26.model.Categoria;
import com.ifpr.leilao26.service.CategoriaService;

@RestController
@CrossOrigin
@RequestMapping("/categoria")
public class CategoriaController {
    @Autowired private CategoriaService serv;

    @PostMapping("/registrar")
    public ResponseEntity<Categoria> criarCategoria(@RequestBody() Categoria categoria){
        Categoria criarCategoria = serv.criarCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(criarCategoria);
    }

    @PutMapping("/atualizar/{id}")
    public Categoria atualizarCategoria(@RequestBody() Categoria categoria, @PathVariable("id") Long id) {
        return serv.atualizarCategoria(categoria);
    }

    @GetMapping("/buscarTodos")
    public List<Categoria> buscarTodos(){
        return serv.buscarTodos();
    }

    @GetMapping("/buscar/{id}")
    public Categoria buscarPorId(@PathVariable() Long id){
        return serv.buscarPorId(id);
    }

    @GetMapping("/buscar/{nome}")
    public Categoria buscarPorNome(@PathVariable() String nome){
        return serv.buscarPorNome(nome);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirCategoria(@PathVariable() Long id){
        serv.excluirCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
