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

import com.ifpr.leilao26.model.Feedback;
import com.ifpr.leilao26.service.FeedbackService;

@RestController
@CrossOrigin
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired private FeedbackService serv;

    @PostMapping("/registrar")
    public ResponseEntity<Feedback> criarFeedback(@RequestBody() Feedback feedback){
        Feedback criarFeedback = serv.criarFeedback(feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(criarFeedback);
    }

    @PutMapping("/atualizar/{id}")
    public Feedback atualizarFeedback(@RequestBody() Feedback feedback, @PathVariable("id") Long id){
        return serv.atualizarFeedback(feedback);
    }

    @GetMapping("/buscar")
    public List<Feedback> buscarTodos(){
        return serv.buscarTodos();
    }

    @GetMapping("/buscar/{id}")
    public Feedback buscarPorId(@PathVariable("id") Long id){
        return serv.buscarPorId(id);
    }

    @DeleteMapping("/excluir/{id}")
    public void excluirFeedback(@PathVariable("id") Long id){
        serv.excluirFeedback(id);
    }
}
