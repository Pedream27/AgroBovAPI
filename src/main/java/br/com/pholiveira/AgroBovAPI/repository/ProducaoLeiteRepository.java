package br.com.pholiveira.AgroBovAPI.repository;


import br.com.pholiveira.AgroBovAPI.model.ProducaoLeite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProducaoLeiteRepository extends JpaRepository<ProducaoLeite, Long> {
    List<ProducaoLeite> findByVacaIdOrderByDataRegistroAsc(Long vacaId);
    List<ProducaoLeite> findByVacaIdAndDataRegistroBetweenOrderByDataRegistroAsc(Long vacaId, LocalDate startDate, LocalDate endDate);
}