package neocamp_teamcubation.projeto_final.domain.port.out;

import neocamp_teamcubation.projeto_final.entity.Estadio;
import java.util.Optional;

public interface EstadioRepositoryPort {
    Optional<Estadio> findById(Long id);
}
