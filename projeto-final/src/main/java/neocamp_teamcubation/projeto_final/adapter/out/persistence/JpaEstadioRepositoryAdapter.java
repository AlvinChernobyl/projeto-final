package neocamp_teamcubation.projeto_final.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.domain.port.out.EstadioRepositoryPort;
import neocamp_teamcubation.projeto_final.entity.Estadio;
import neocamp_teamcubation.projeto_final.adapter.out.persistence.springdata.EstadioRepo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaEstadioRepositoryAdapter implements EstadioRepositoryPort {
    private final EstadioRepo repo;
    @Override public Optional<Estadio> findById(Long id) { return repo.findById(id); }
}
