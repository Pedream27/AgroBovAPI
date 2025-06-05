package br.com.pholiveira.AgroBovAPI.service;

import br.com.pholiveira.AgroBovAPI.model.Vaca;
import br.com.pholiveira.AgroBovAPI.repository.VacaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VacaService {

    @Autowired
    private VacaRepository vacaRepository;

    public List<Vaca> listarVacas() {
        return vacaRepository.findAll();
    }

    public Optional<Vaca> buscarVacaPorId(Long id) {
        return vacaRepository.findById(id);
    }

    public Vaca salvarVaca(Vaca vaca) {
        return vacaRepository.save(vaca);
    }

    public void deletarVaca(Long id) {
        vacaRepository.deleteById(id);
    }
}
