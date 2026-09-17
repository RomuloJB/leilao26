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

    public Leilao atualizarLeilao(Leilao leilaoAtualizado) {
        Leilao existente = repo.findById(leilaoAtualizado.getId())
            .orElseThrow(() -> new IllegalArgumentException("Leilão não encontrado."));

        existente.setTitulo(leilaoAtualizado.getTitulo());
        existente.setDescricao(leilaoAtualizado.getDescricao());
        existente.setDescricaoDetalhada(leilaoAtualizado.getDescricaoDetalhada());
        existente.setDataHoraInicio(leilaoAtualizado.getDataHoraInicio());
        existente.setDataHoraFim(leilaoAtualizado.getDataHoraFim());
        existente.setStatus(leilaoAtualizado.getStatus());
        existente.setObservacao(leilaoAtualizado.getObservacao());
        existente.setValorIncremento(leilaoAtualizado.getValorIncremento());
        existente.setLanceMinimo(leilaoAtualizado.getLanceMinimo());

        if (leilaoAtualizado.getCategoria() != null) {
            existente.setCategoria(leilaoAtualizado.getCategoria());
        }

        return repo.save(existente);
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

    // Regra: só leilão ENCERRADO ou CANCELADO pode ser excluído — vale pro ADMIN também.
    // Quem (ADMIN / vendedor dono) pode chamar é checado no LeilaoController.verificarPermissao.
    public void excluirLeilao(Long id){
        Leilao leilao = repo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Leilão não encontrado."));

        if (!podeSerExcluido(leilao)) {
            throw new IllegalArgumentException(
                "Só é possível excluir leilões com status ENCERRADO ou CANCELADO. "
                + "Cancele ou encerre o leilão antes de excluí-lo.");
        }

        repo.delete(leilao);
    }

    public static boolean podeSerExcluido(Leilao leilao) {
        StatusLeilao status = leilao.getStatus();
        return status == StatusLeilao.ENCERRADO || status == StatusLeilao.CANCELADO;
    }
}