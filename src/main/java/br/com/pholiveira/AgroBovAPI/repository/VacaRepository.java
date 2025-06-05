package br.com.pholiveira.AgroBovAPI.repository;

import br.com.pholiveira.AgroBovAPI.model.Vaca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacaRepository extends JpaRepository<Vaca, Long> {
}
