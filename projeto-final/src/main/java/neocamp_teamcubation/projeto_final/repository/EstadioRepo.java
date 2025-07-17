package neocamp_teamcubation.projeto_final.repository;

import neocamp_teamcubation.projeto_final.entity.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadioRepo extends JpaRepository<Estadio, Long> {
    boolean existsByNome(String nome);
}
