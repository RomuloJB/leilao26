package com.ifpr.leilao26.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpr.leilao26.enums.StatusLeilao;
import com.ifpr.leilao26.model.Leilao;
import com.ifpr.leilao26.repository.LeilaoRepository;

@Service
public class LeilaoService {
    @Autowired private LeilaoRepository repo;

    public Leilao criarLeilao(Leilao leilao){
        return repo.save(leilao);
    }

    public Leilao atualizarLeilao(Leilao leilao) {
        return repo.save(leilao);
    }

    public List<Leilao> buscarTodos(){
        return repo.findAll();
    }

    public Leilao buscarPorId(Long id){
        return repo.findById(id).orElse(null);
    }

    public Leilao buscarPorTitulo(String titulo){
        return repo.findByTitulo(titulo);
    }

    public Leilao buscarPorDataHoraInicio(LocalDateTime dataHoraInicio){
        return repo.findByDataHoraInicio(dataHoraInicio);
    }

    public Leilao buscarPorDataHoraFim(LocalDateTime dataHoraFim){
        return repo.findByDataHoraInicio(dataHoraFim);
    }

    public Leilao buscarPorStatusLeilao(StatusLeilao status){
        return repo.findByStatus(status);
    }

    public Leilao buscarPorValorIncremento(Float valorIncrmento){
        return repo.findByValorIncremento(valorIncrmento);
    }

    public Leilao buscarPorLanceMinimo(Float lanceMinimo){
        return repo.findByLanceMinimo(lanceMinimo);
    }

    public void excluirLeilao(Long id){
        repo.deleteById(id);
    }
}
