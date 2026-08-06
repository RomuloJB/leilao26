package com.ifpr.leilao26.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.model.Pagamento;
import com.ifpr.leilao26.repository.PagamentoRepository;

@Service
public class PagamentoService {
    @Autowired private PagamentoRepository repo;

    public Pagamento criarPagamento(Pagamento pagamento) {
        return repo.save(pagamento);
    }

    public Pagamento atualizarPagamento(Pagamento pagamento){
        return repo.save(pagamento);
    }

    public List<Pagamento> buscarTodos(){
        return repo.findAll();
    }

    public Pagamento buscarPorId(Long id){
        return repo.findById(id).orElse(null);
    }

    public List<Pagamento> buscarPorDataHora(LocalDateTime dataHora){
        return repo.findByDataHora(dataHora);
    }

    public List<Pagamento> buscarPorStatus(String status){
        return repo.findByStatus(status);
    }

    public void excluirPagamento(Long id){
        repo.deleteById(id);
    }
}
