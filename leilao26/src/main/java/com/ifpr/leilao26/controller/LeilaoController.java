package com.ifpr.leilao26.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ifpr.leilao26.dto.LeilaoResponseDTO;
import com.ifpr.leilao26.enums.StatusLeilao;
import com.ifpr.leilao26.enums.TipoPerfil;
import com.ifpr.leilao26.model.Leilao;
import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.service.LeilaoService;

@RestController
@CrossOrigin
@RequestMapping("/leilao")
public class LeilaoController {
    @Autowired private LeilaoService serv;

    @PostMapping("/registrar")
    public ResponseEntity<LeilaoResponseDTO> criarLeilao(@RequestBody Leilao leilao,
                                                          @AuthenticationPrincipal Pessoa pessoaLogada) {
        leilao.setVendedor(pessoaLogada);
        Leilao criado = serv.criarLeilao(leilao);
        return ResponseEntity.status(HttpStatus.CREATED).body(LeilaoResponseDTO.from(criado));
    }

    @PutMapping("/atualizar/{id}")
    public LeilaoResponseDTO atualizarLeilao(@RequestBody Leilao leilao,
                                              @PathVariable("id") Long id,
                                              @AuthenticationPrincipal Pessoa pessoaLogada) {
        Leilao existente = serv.buscarPorId(id);
        if (existente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Leilão não encontrado.");
        }
        verificarPermissao(existente, pessoaLogada);

        leilao.setId(id);
        leilao.setVendedor(existente.getVendedor());
        return LeilaoResponseDTO.from(serv.atualizarLeilao(leilao));
    }

    @GetMapping("/buscar")
    public List<LeilaoResponseDTO> buscarTodos(){
        return serv.buscarTodos().stream().map(LeilaoResponseDTO::from).toList();
    }

    @GetMapping("/buscar/id/{id}")
    public LeilaoResponseDTO buscarPorId(@PathVariable() Long id) {
        Leilao leilao = serv.buscarPorId(id);
        if (leilao == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Leilão não encontrado.");
        }
        return LeilaoResponseDTO.from(leilao);
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
    public ResponseEntity<Void> excluirLeilao(@PathVariable Long id,
                                               @AuthenticationPrincipal Pessoa pessoaLogada) {
        Leilao existente = serv.buscarPorId(id);
        if (existente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Leilão não encontrado.");
        }
        verificarPermissao(existente, pessoaLogada);

        serv.excluirLeilao(id);
        return ResponseEntity.noContent().build();
    }

    // Regra: ADMIN pode alterar qualquer leilão; VENDEDOR só os que ele mesmo criou.
    // O SecurityConfig já barra COMPRADOR antes de chegar aqui, mas a checagem de perfil
    // fica também aqui pra regra ser completa mesmo se a config de rotas mudar.
    private void verificarPermissao(Leilao leilao, Pessoa pessoaLogada) {
        boolean isAdmin = temPerfil(pessoaLogada, TipoPerfil.ADMIN);
        boolean isVendedor = temPerfil(pessoaLogada, TipoPerfil.VENDEDOR);
        boolean isDono = leilao.getVendedor() != null
            && leilao.getVendedor().getId().equals(pessoaLogada.getId());

        if (!isAdmin && !(isVendedor && isDono)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Você não tem permissão para alterar este leilão.");
        }
    }

    private boolean temPerfil(Pessoa pessoa, TipoPerfil tipo) {
        return pessoa.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_" + tipo.name()));
    }
}