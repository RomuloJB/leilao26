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

import com.ifpr.leilao26.enums.StatusLeilao;
import com.ifpr.leilao26.model.Leilao;
import com.ifpr.leilao26.service.LeilaoService;

@RestController
@CrossOrigin
@RequestMapping("/leilao")
public class LeilaoController {
    @Autowired private LeilaoService serv;

    @PostMapping("/registrar")
    public ResponseEntity<Leilao> criarLeilao(@RequestBody() Leilao leilao) {
        Leilao criarLeilao = serv.criarLeilao(leilao);
        return ResponseEntity.status(HttpStatus.CREATED).body(criarLeilao);
    }

    @PutMapping("/atualizar/{id}")
    public Leilao atualizarLeilao(@RequestBody() Leilao leilao, @PathVariable("id") Long id) {
        return serv.atualizarLeilao(leilao);
    }

    @GetMapping("/buscar")
    public List<Leilao> buscarTodos(){
        return serv.buscarTodos();
    }

    @GetMapping("/buscar/{id}")
    public Leilao buscarPorId(@PathVariable() Long id) {
        return serv.buscarPorId(id);
    }
    
    @GetMapping("/buscar/titulo/{titulo}")
    public Leilao buscarPorTitulo(@PathVariable String titulo) {
        return serv.buscarPorTitulo(titulo);
    }

    @GetMapping("/buscar/inicio/{dataHoraInicio}")
    public Leilao buscarPorDataHoraInicio(@PathVariable LocalDateTime dataHoraInicio) {
        return serv.buscarPorDataHoraInicio(dataHoraInicio);
    }

    @GetMapping("/buscar/fim/{dataHoraFim}")
    public Leilao buscarPorDataHoraFim(@PathVariable LocalDateTime dataHoraFim) {
        return serv.buscarPorDataHoraFim(dataHoraFim);
    }

    @GetMapping("/buscar/status/{status}")
    public Leilao buscarPorStatus(@PathVariable StatusLeilao status) {
        return serv.buscarPorStatusLeilao(status);
    }

    @GetMapping("/buscar/incremento/{valorIncremento}")
    public Leilao buscarPorValorIncremento(@PathVariable Float valorIncremento) {
        return serv.buscarPorValorIncremento(valorIncremento);
    }

    @GetMapping("/buscar/lanceMinimo/{lanceMinimo}")
    public Leilao buscarPorLanceMinimo(@PathVariable Float lanceMinimo) {
        return serv.buscarPorLanceMinimo(lanceMinimo);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirLeilao(@PathVariable Long id) {
        serv.excluirLeilao(id);
        return ResponseEntity.noContent().build();
    }
}
