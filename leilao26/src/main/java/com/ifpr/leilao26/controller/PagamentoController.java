package com.ifpr.leilao26.controller;

import java.time.LocalDateTime;
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

import com.ifpr.leilao26.model.Pagamento;
import com.ifpr.leilao26.service.PagamentoService;

@RestController
@CrossOrigin
@RequestMapping("/pagamento")
public class PagamentoController {
    @Autowired private PagamentoService serv;

    @PostMapping("/registrar")
    public ResponseEntity<Pagamento> criarPagamento(@RequestBody() Pagamento pagamento){
        Pagamento criarPagamento = serv.criarPagamento(pagamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(criarPagamento);
    }

    @PutMapping("/atualizar/{id}")
    public Pagamento atualizarPagamento(@RequestBody() Pagamento pagamento, @PathVariable("id") Long id){
        return serv.atualizarPagamento(pagamento);
    }

    @GetMapping("/buscar")
    public List<Pagamento> buscarTodos(){
        return serv.buscarTodos();
    }

    @GetMapping("/buscar/{id}")
    public Pagamento buscarPorId(@PathVariable("id") Long id){
        return serv.buscarPorId(id);
    }

    @GetMapping("/buscar/{dataHora}")
    public List<Pagamento> buscarPorDataHora(@PathVariable("dataHora") LocalDateTime dataHora){
        return serv.buscarPorDataHora(dataHora);
    }

    @GetMapping("/buscar/{status}")
    public List<Pagamento> buscarPorStatus(@PathVariable("status") String status){
        return serv.buscarPorStatus(status);
    }

    @DeleteMapping("/excluir/{id}")
    public void excluirPagamento(@PathVariable("id") Long id){
        serv.excluirPagamento(id);
    }
}
