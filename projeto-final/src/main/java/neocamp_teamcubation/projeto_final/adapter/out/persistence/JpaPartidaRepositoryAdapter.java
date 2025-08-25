package neocamp_teamcubation.projeto_final.adapter.out.persistence;


import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.domain.port.out.PartidaRepositoryPort;
import neocamp_teamcubation.projeto_final.entity.Partida;
import neocamp_teamcubation.projeto_final.adapter.out.persistence.springdata.PartidaRepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaPartidaRepositoryAdapter implements PartidaRepositoryPort {

    private final PartidaRepo repo;

    @Override public Partida save(Partida p) { return repo.save(p); }
    @Override public Optional<Partida> findById(Long id) { return repo.findById(id); }
    @Override public List<Partida> findAll() { return repo.findAll(); }
    @Override public void deleteById(Long id) { repo.deleteById(id); }
}
