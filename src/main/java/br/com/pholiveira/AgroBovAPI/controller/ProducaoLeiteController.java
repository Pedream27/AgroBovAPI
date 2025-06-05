package br.com.pholiveira.AgroBovAPI.controller;

import br.com.pholiveira.AgroBovAPI.model.ProducaoLeite;
import br.com.pholiveira.AgroBovAPI.service.ProducaoLeiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static br.com.pholiveira.AgroBovAPI.util.AddLinkHateos.addLink;

@RestController
@RequestMapping("/api/producao-leite")
public class ProducaoLeiteController {

    @Autowired
    private ProducaoLeiteService producaoLeiteService;

    @PostMapping("/vaca/{vacaId}")
    public ResponseEntity<ProducaoLeite> registrarProducao(@PathVariable Long vacaId, @RequestBody ProducaoLeite producaoLeite) {
        try {
            ProducaoLeite novaProducao = producaoLeiteService.registrarProducao(vacaId, producaoLeite);
            addLink(vacaId , producaoLeite);
            return new ResponseEntity<>(novaProducao, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Vaca não encontrada
        }
    }

    @GetMapping("/vaca/{vacaId}")
    public ResponseEntity<List<ProducaoLeite>> getProducoesPorVaca(@PathVariable Long vacaId) {
        List<ProducaoLeite> producoes = producaoLeiteService.buscarProducoesPorVaca(vacaId);
        if (producoes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        for (ProducaoLeite producaoLeite : producoes) {
            addLink(vacaId , producaoLeite);
        }
        return new ResponseEntity<>(producoes, HttpStatus.OK);
    }

    @GetMapping("/vaca/{vacaId}/analise-15-dias")
    public ResponseEntity<List<Map<String, Object>>> analisarProducao15Dias(@PathVariable Long vacaId) {
        List<Map<String, Object>> analise = producaoLeiteService.analisarProducaoUltimos15Dias(vacaId);
        if (analise.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(analise, HttpStatus.OK);
    }
}
