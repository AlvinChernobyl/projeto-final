package neocamp_teamcubation.projeto_final.repository;

import neocamp_teamcubation.projeto_final.entity.Clube;
import neocamp_teamcubation.projeto_final.entity.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadioRepo extends JpaRepository<Estadio, Long> {
    Optional<Estadio> findByNome(String nome);
    boolean existsByNome(String nome);
}
