package com.ifpr.leilao26.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.model.Lance;
import com.ifpr.leilao26.repository.LanceRepository;

@Service
public class LanceService {
    @Autowired private LanceRepository repo;

    public Lance criarLance(Lance lance) {
        return repo.save(lance);
    }

    public Lance atualizarLance(Lance lance){
        return repo.save(lance);
    }

    public List<Lance> buscarTodos(){
        return repo.findAll();
    }

    public Lance buscarPorId(Long id){
        return repo.findById(id).orElse(null);
    }

    public Lance buscarPorValorLance(Float valorLance){
        return repo.findByValorLance(valorLance);
    }

    public Lance buscarPorDataHora(LocalDateTime dataHora){
        return repo.findByDataHora(dataHora);
    }

    public void excluirLance(Long id){
        repo.deleteById(id);
    }
}
