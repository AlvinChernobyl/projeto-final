package neocamp_teamcubation.projeto_final.adapter.out.persistence.springdata;

import neocamp_teamcubation.projeto_final.entity.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidaRepo extends JpaRepository<Partida, Long> {
}
