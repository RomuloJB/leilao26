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

import com.ifpr.leilao26.model.Lance;
import com.ifpr.leilao26.service.LanceService;

@RestController
@CrossOrigin
@RequestMapping("/lance")
public class LanceController {
    @Autowired private LanceService serv;

    @PostMapping("/registrar")
    public ResponseEntity<Lance> criarLance(@RequestBody() Lance lance) {
        Lance criarLance = serv.criarLance(lance);
        return ResponseEntity.status(HttpStatus.CREATED).body(criarLance);
    }

    @PutMapping("/atualizar/{id}")
    public Lance atualizarLance(@RequestBody() Lance lance, @PathVariable("id") Long id){
        return serv.atualizarLance(lance);
    }

    @GetMapping("/buscar")
    public List<Lance> buscarTodos(){
        return serv.buscarTodos();
    }

    @GetMapping("/buscar/{id}")
    public Lance buscarPorId(@PathVariable("id") Long id){
        return serv.buscarPorId(id);
    }

    @GetMapping("/buscar/{valorLance}")
    public Lance buscarPorValorLance(@PathVariable("valorLance") Float valorLance){
        return serv.buscarPorValorLance(valorLance);
    }

    @GetMapping("/buscar/{dataHora}")
    public Lance buscarPorDataHora(@PathVariable("dataHora") LocalDateTime dataHora){
        return serv.buscarPorDataHora(dataHora);
    }

    @DeleteMapping("excluir/{id}")
    public void excluirLance(@PathVariable("id") Long id){
        serv.excluirLance(id);
    }
}
