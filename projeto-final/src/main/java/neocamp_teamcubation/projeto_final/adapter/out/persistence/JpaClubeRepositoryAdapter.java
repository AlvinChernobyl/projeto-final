package neocamp_teamcubation.projeto_final.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.adapter.out.persistence.springdata.ClubeRepo;
import neocamp_teamcubation.projeto_final.domain.port.out.ClubeRepositoryPort;
import neocamp_teamcubation.projeto_final.entity.Clube;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaClubeRepositoryAdapter implements ClubeRepositoryPort {

    private final ClubeRepo repo;

    @Override public Clube save(Clube c) { return repo.save(c); }
    @Override public Optional<Clube> findById(Long id) { return repo.findById(id); }
    @Override public List<Clube> findAll() { return repo.findAll(); }
    @Override public List<Clube> findByAtivo(Boolean ativo) { return repo.findByAtivo(ativo); }
    @Override public boolean existsByNomeAndSiglaEstado(String nome, String siglaEstado) {
        return repo.existsByNomeAndSiglaEstado(nome, siglaEstado);
    }
}
