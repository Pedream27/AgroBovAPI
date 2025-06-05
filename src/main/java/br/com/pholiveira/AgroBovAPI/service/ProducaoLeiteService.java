package br.com.pholiveira.AgroBovAPI.service;


import br.com.pholiveira.AgroBovAPI.model.ProducaoLeite;
import br.com.pholiveira.AgroBovAPI.model.Vaca;
import br.com.pholiveira.AgroBovAPI.repository.ProducaoLeiteRepository;
import br.com.pholiveira.AgroBovAPI.repository.VacaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ProducaoLeiteService {

    @Autowired
    private ProducaoLeiteRepository producaoLeiteRepository;

    @Autowired
    private VacaRepository vacaRepository;

    public ProducaoLeite registrarProducao(Long vacaId, ProducaoLeite producaoLeite) {
        Optional<Vaca> vacaOptional = vacaRepository.findById(vacaId);
        if (vacaOptional.isPresent()) {
            producaoLeite.setVaca(vacaOptional.get());
            return producaoLeiteRepository.save(producaoLeite);
        }
        throw new RuntimeException("Vaca não encontrada com o ID: " + vacaId);
    }

    public List<ProducaoLeite> buscarProducoesPorVaca(Long vacaId) {
        return producaoLeiteRepository.findByVacaIdOrderByDataRegistroAsc(vacaId);
    }

    /**
     * Calcula a variação da produção de leite para uma vaca nos últimos 15 dias.
     *
     * @param vacaId O ID da vaca.
     * @return Um mapa com a data do registro, a quantidade de litros e a variação em relação ao dia anterior (se houver).
     */
    public List<Map<String, Object>> analisarProducaoUltimos15Dias(Long vacaId) {
        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(14); // 15 dias incluindo o dia de hoje

        List<ProducaoLeite> producoes = producaoLeiteRepository
                .findByVacaIdAndDataRegistroBetweenOrderByDataRegistroAsc(vacaId, quinzeDiasAtras, hoje);

        List<Map<String, Object>> resultados = new ArrayList<>();
        Double producaoAnterior = null;

        for (ProducaoLeite producao : producoes) {
            Map<String, Object> registro = new HashMap<>();
            registro.put("dataRegistro", producao.getDataRegistro());
            registro.put("quantidadeLitros", producao.getQuantidadeLitros());

            if (producaoAnterior != null) {
                double variacao = producao.getQuantidadeLitros() - producaoAnterior;
                registro.put("variacaoLitros", variacao);
                if (variacao > 0) {
                    registro.put("tendencia", "Aumentou");
                } else if (variacao < 0) {
                    registro.put("tendencia", "Diminuiu");
                } else {
                    registro.put("tendencia", "Estável");
                }
            } else {
                registro.put("variacaoLitros", null);
                registro.put("tendencia", "Primeiro registro no período");
            }
            producaoAnterior = producao.getQuantidadeLitros();
            resultados.add(registro);
        }
        return resultados;
    }
}
