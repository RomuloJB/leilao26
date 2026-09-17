package com.ifpr.leilao26.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ifpr.leilao26.enums.StatusLeilao;
import com.ifpr.leilao26.model.Lance;
import com.ifpr.leilao26.model.Leilao;
import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.repository.LanceRepository;
import com.ifpr.leilao26.repository.LeilaoRepository;

@Service
public class LanceService {
    @Autowired private LanceRepository repo;
    @Autowired private LeilaoRepository leilaoRepo;

    /**
     * Registra um lance do comprador logado em um leilão.
     *
     * Regras (todas lançam IllegalArgumentException com mensagem pro usuário):
     *  - o leilão precisa existir e não estar ENCERRADO/CANCELADO;
     *  - o vendedor não pode dar lance no próprio leilão;
     *  - o valor não pode ser menor que o lance mínimo do leilão;
     *  - se já houver lances, o novo precisa ser >= maior lance + valorIncremento.
     */
    @Transactional
    public Lance registrarLance(Long leilaoId, Float valorLance, Pessoa comprador) {
        if (leilaoId == null) {
            throw new IllegalArgumentException("Informe o leilão.");
        }
        if (valorLance == null || valorLance <= 0) {
            throw new IllegalArgumentException("Informe um valor de lance válido.");
        }

        Leilao leilao = leilaoRepo.findById(leilaoId)
            .orElseThrow(() -> new IllegalArgumentException("Leilão não encontrado."));

        if (leilao.getStatus() == StatusLeilao.ENCERRADO || leilao.getStatus() == StatusLeilao.CANCELADO) {
            throw new IllegalArgumentException("Este leilão não está mais aceitando lances.");
        }

        if (leilao.getVendedor() != null && leilao.getVendedor().getId().equals(comprador.getId())) {
            throw new IllegalArgumentException("Você não pode dar lance no seu próprio leilão.");
        }

        Float valorMinimoPermitido = calcularValorMinimoPermitido(leilao);
        if (valorLance < valorMinimoPermitido) {
            throw new IllegalArgumentException(
                String.format("O lance deve ser de no mínimo R$ %.2f.", valorMinimoPermitido));
        }

        Lance lance = new Lance();
        lance.setLeilao(leilao);
        lance.setPessoa(comprador);
        lance.setValorLance(valorLance);
        lance.setDataHora(LocalDateTime.now());
        return repo.save(lance);
    }

    /**
     * Menor valor aceito para o próximo lance: o lance mínimo do leilão se ainda não houve
     * lances, senão o maior lance atual somado ao incremento do leilão.
     */
    public Float calcularValorMinimoPermitido(Leilao leilao) {
        Optional<Lance> maiorLance = repo.findFirstByLeilaoIdOrderByValorLanceDesc(leilao.getId());
        if (maiorLance.isEmpty()) {
            return leilao.getLanceMinimo();
        }
        float incremento = leilao.getValorIncremento() != null ? leilao.getValorIncremento() : 0f;
        return Math.max(leilao.getLanceMinimo(), maiorLance.get().getValorLance() + incremento);
    }

    public List<Lance> buscarPorLeilao(Long leilaoId) {
        return repo.findByLeilaoIdOrderByValorLanceDescDataHoraAsc(leilaoId);
    }

    public List<Lance> buscarTodos(){
        return repo.findAll();
    }

    public Lance buscarPorId(Long id){
        return repo.findById(id).orElse(null);
    }

    public void excluirLance(Long id){
        repo.deleteById(id);
    }
}
