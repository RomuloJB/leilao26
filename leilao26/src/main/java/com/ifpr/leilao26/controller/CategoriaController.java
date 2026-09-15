package com.ifpr.leilao26.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpr.leilao26.dto.CategoriaResponseDTO;
import com.ifpr.leilao26.model.Categoria;
import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.service.CategoriaService;

@RestController
@CrossOrigin
@RequestMapping("/categoria")
public class CategoriaController {
    @Autowired private CategoriaService serv;
    @PostMapping("/registrar")
    public ResponseEntity<CategoriaResponseDTO> criarCategoria(@RequestBody Categoria categoria,
                                                    @AuthenticationPrincipal Pessoa pessoaLogada){
        categoria.setCriado_por(pessoaLogada);
        Categoria criada = serv.criarCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaResponseDTO.from(criada));
    }

    @PutMapping("/atualizar/{id}")
    public CategoriaResponseDTO atualizarCategoria(@RequestBody() Categoria categoria, @PathVariable("id") Long id) {
        categoria.setId(id);
        return CategoriaResponseDTO.from(serv.atualizarCategoria(categoria));
    }

    @GetMapping("/buscar")
    public List<CategoriaResponseDTO> buscarTodos(){
        return serv.buscarTodos().stream().map(CategoriaResponseDTO::from).toList();
    }

    @GetMapping("/buscar/id/{id}")
    public CategoriaResponseDTO buscarPorId(@PathVariable() Long id){
        return CategoriaResponseDTO.from(serv.buscarPorId(id));
    }

    @GetMapping("/buscar/nome/{nome}")
    public CategoriaResponseDTO buscarPorNome(@PathVariable() String nome){
        return CategoriaResponseDTO.from(serv.buscarPorNome(nome));
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirCategoria(@PathVariable() Long id){
        serv.excluirCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
