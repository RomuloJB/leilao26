package com.ifpr.leilao26.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ifpr.leilao26.dto.LanceRequestDTO;
import com.ifpr.leilao26.dto.LanceResponseDTO;
import com.ifpr.leilao26.model.Lance;
import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.service.LanceService;

@RestController
@CrossOrigin
@RequestMapping("/lance")
public class LanceController {
    @Autowired private LanceService serv;

    // Quem pode chamar (só COMPRADOR) é definido no SecurityConfig.
    // As regras de valor mínimo/incremento/status ficam no LanceService.
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarLance(@RequestBody LanceRequestDTO request,
                                            @AuthenticationPrincipal Pessoa pessoaLogada) {
        try {
            Lance criado = serv.registrarLance(request.getLeilaoId(), request.getValorLance(), pessoaLogada);
            return ResponseEntity.status(HttpStatus.CREATED).body(LanceResponseDTO.from(criado));
        } catch (IllegalArgumentException e) {
            // Mesmo formato de erro do AuthController, pro front exibir e.response.data.message.
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/buscar/leilao/{leilaoId}")
    public List<LanceResponseDTO> buscarPorLeilao(@PathVariable Long leilaoId) {
        return serv.buscarPorLeilao(leilaoId).stream().map(LanceResponseDTO::from).toList();
    }

    @GetMapping("/buscar")
    public List<LanceResponseDTO> buscarTodos(){
        return serv.buscarTodos().stream().map(LanceResponseDTO::from).toList();
    }

    @GetMapping("/buscar/id/{id}")
    public LanceResponseDTO buscarPorId(@PathVariable Long id){
        Lance lance = serv.buscarPorId(id);
        if (lance == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lance não encontrado.");
        }
        return LanceResponseDTO.from(lance);
    }

    // Lance não se edita: uma vez dado, vale. Só ADMIN exclui (restrito no SecurityConfig).
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirLance(@PathVariable Long id){
        if (serv.buscarPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lance não encontrado.");
        }
        serv.excluirLance(id);
        return ResponseEntity.noContent().build();
    }
}
