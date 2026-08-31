package com.ifpr.leilao26.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ifpr.leilao26.dto.ImagemResponseDTO;
import com.ifpr.leilao26.model.Imagem;
import com.ifpr.leilao26.model.Leilao;
import com.ifpr.leilao26.model.Pessoa;
import com.ifpr.leilao26.service.ImagemService;
import com.ifpr.leilao26.service.LeilaoService;

@RestController
@CrossOrigin
@RequestMapping("/imagem")
public class ImagemController {
    @Autowired private ImagemService serv;
    @Autowired private LeilaoService leilaoServ;

    @PostMapping("/upload")
    public ResponseEntity<ImagemResponseDTO> upload(@RequestParam("arquivo") MultipartFile arquivo,
                                                      @RequestParam("leilaoId") Long leilaoId,
                                                      @AuthenticationPrincipal Pessoa pessoaLogada) throws IOException {
        Leilao leilao = leilaoServ.buscarPorId(leilaoId);
        if (leilao == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Leilão não encontrado.");
        }
        verificarPermissao(leilao, pessoaLogada);

        Imagem salva = serv.salvarImagem(arquivo, leilaoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ImagemResponseDTO.from(salva));
    }

    // serve os bytes da imagem guardados no banco — usado no src="" do <img>
    @GetMapping("/arquivo/{id}")
    public ResponseEntity<byte[]> baixarArquivo(@PathVariable Long id) {
        Imagem imagem = serv.buscarPorId(id);
        if (imagem == null || imagem.getArquivo() == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType tipo = imagem.getTipoConteudo() != null
            ? MediaType.parseMediaType(imagem.getTipoConteudo())
            : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok().contentType(tipo).body(imagem.getArquivo());
    }

    @GetMapping("/buscar")
    public List<ImagemResponseDTO> buscarTodos(){
        return serv.buscarTodos().stream().map(ImagemResponseDTO::from).toList();
    }

    @GetMapping("/buscar/id/{id}")
    public ImagemResponseDTO buscarPorId(@PathVariable("id") Long id){
        return ImagemResponseDTO.from(serv.buscarPorId(id));
    }

    @DeleteMapping("/excluir/{id}")
    public void excluirImagem(@PathVariable("id") Long id, @AuthenticationPrincipal Pessoa pessoaLogada){
        Imagem imagem = serv.buscarPorId(id);
        if (imagem == null) return;
        verificarPermissao(imagem.getLeilao(), pessoaLogada);
        serv.excluirImagem(id);
    }

    private void verificarPermissao(Leilao leilao, Pessoa pessoaLogada) {
        boolean isDono = leilao.getVendedor() != null
            && leilao.getVendedor().getId().equals(pessoaLogada.getId());
        boolean isAdmin = pessoaLogada.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isDono && !isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Você não tem permissão para alterar as imagens deste leilão.");
        }
    }
}