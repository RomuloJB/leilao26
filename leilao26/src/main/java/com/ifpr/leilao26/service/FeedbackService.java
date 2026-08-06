package com.ifpr.leilao26.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.model.Feedback;
import com.ifpr.leilao26.repository.FeedbackRepository;

@Service
public class FeedbackService {
    @Autowired private FeedbackRepository repo;

    public Feedback criarFeedback(Feedback feedback) {
        return repo.save(feedback);
    }

    public Feedback atualizarFeedback(Feedback feedback) {
        return repo.save(feedback);
    }

    public List<Feedback> buscarTodos(){
        return repo.findAll();
    }

    public Feedback buscarPorId(Long id){
        return repo.findById(id).orElse(null);
    }

    public void excluirFeedback(Long id){
        repo.deleteById(id);
    }
}
