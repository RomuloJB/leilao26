package com.ifpr.leilao26.controller;

import java.util.List;
import java.util.Map;

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

import com.ifpr.leilao26.dto.TrocarSenhaRequest;
import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.service.PessoaService;

@RestController
@RequestMapping("/pessoa")
@CrossOrigin
public class PessoaController {
    @Autowired private PessoaService serv;
    
    @GetMapping("/buscar")
    public List<Pessoa> buscarTodos(){
        return serv.buscarTodos();
    }
    
    @GetMapping("/buscar/id/{id}")
    public Pessoa buscarPessoaPorId(@PathVariable Long id) {
        return serv.buscarPessoaPorId(id);
    }
    
    @PostMapping("/registrar")
    public ResponseEntity<Pessoa> criarPessoa(@RequestBody() Pessoa pessoa){
        Pessoa criarPessoa = serv.criarPessoa(pessoa);
        return ResponseEntity.status(HttpStatus.CREATED).body(criarPessoa);
    }

    @PutMapping("/atualizar/{id}")
    public Pessoa atualizarPessoa(@RequestBody() Pessoa pessoa, @PathVariable("id") Long id) {
        return serv.atualizarPessoa(pessoa);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirPessoa(@PathVariable Long id){
        serv.excluirPessoa(id);
        return ResponseEntity.noContent().build();
    }

    // Troca a senha do próprio usuário logado (o id vem do token, não do corpo da requisição)
    @PutMapping("/trocar-senha")
    public ResponseEntity<Map<String, String>> trocarSenha(@RequestBody TrocarSenhaRequest request,
                                                           @AuthenticationPrincipal Pessoa pessoaLogada) {
        try {
            serv.trocarSenha(pessoaLogada.getId(), request.getSenhaAtual(), request.getNovaSenha());
            return ResponseEntity.ok(Map.of("message", "Senha alterada com sucesso."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
