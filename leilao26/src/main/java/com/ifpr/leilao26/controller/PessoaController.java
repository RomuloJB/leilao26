package com.ifpr.leilao26.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.service.PessoaService;

@RestController
@RequestMapping("/pessoa")
@CrossOrigin
public class PessoaController {
    @Autowired private PessoaService serv;

    @GetMapping
    public List<Pessoa> findAll(){
        return serv.findAll();
    }
}
