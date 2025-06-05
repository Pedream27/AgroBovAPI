package br.com.pholiveira.AgroBovAPI.controller;

import br.com.pholiveira.AgroBovAPI.model.Vaca;
import br.com.pholiveira.AgroBovAPI.service.VacaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static br.com.pholiveira.AgroBovAPI.util.AddLinkHateos.addLink;

@RestController
@RequestMapping("/api/vacas")
public class VacaController {

    @Autowired
    private VacaService vacaService;

    @GetMapping
    public ResponseEntity<List<Vaca>> getAllVacas() {
        List<Vaca> vacas = vacaService.listarVacas();
        for (var vaca : vacas) {
        addLink(vaca);
        }
        return new ResponseEntity<>(vacas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vaca> getVacaById(@PathVariable Long id) {
        return vacaService.buscarVacaPorId(id)
                .map(vaca -> {
                    // Adicionar links para a vaca individual
                      addLink(vaca);
                    return new ResponseEntity<>(vaca, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Vaca> createVaca(@RequestBody Vaca vaca) {
        Vaca novaVaca = vacaService.salvarVaca(vaca);
        addLink(novaVaca);
        return new ResponseEntity<>(novaVaca, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vaca> updateVaca(@PathVariable Long id, @RequestBody Vaca vaca) {
        return vacaService.buscarVacaPorId(id)
                .map(vacaExistente -> {
                    vacaExistente.setNome(vaca.getNome());
                    vacaExistente.setRaca(vaca.getRaca());
                    vacaExistente.setDataNascimento(vaca.getDataNascimento());
                    Vaca vacaAtualizada = vacaService.salvarVaca(vacaExistente);
                    // Adicionar links após a atualização
                    addLink(vacaAtualizada);
                    return new ResponseEntity<>(vacaAtualizada, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVaca(@PathVariable Long id) {
        if (vacaService.buscarVacaPorId(id).isPresent()) {
            vacaService.deletarVaca(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
