package br.com.geomottu.api.repository;

import br.com.geomottu.api.model.entities.Filial;
import br.com.geomottu.api.model.entities.Patio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatioRepository extends JpaRepository<Patio, Long> {
    List<Patio> findAllByFilial(Filial filial);
    Optional<Patio> findByIdAndFilial(Long id, Filial filial);

}
