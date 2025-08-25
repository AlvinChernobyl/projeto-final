package neocamp_teamcubation.projeto_final.domain.port.out;

import neocamp_teamcubation.projeto_final.entity.Partida;
import java.util.List;
import java.util.Optional;

public interface PartidaRepositoryPort {
    Partida save(Partida p);
    Optional<Partida> findById(Long id);
    List<Partida> findAll();
    void deleteById(Long id);
}
