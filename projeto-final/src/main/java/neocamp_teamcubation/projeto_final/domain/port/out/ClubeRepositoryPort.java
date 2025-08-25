package neocamp_teamcubation.projeto_final.domain.port.out;


import neocamp_teamcubation.projeto_final.entity.Clube;

import java.util.List;
import java.util.Optional;

public interface ClubeRepositoryPort {
    Clube save(Clube c);
    Optional<Clube> findById(Long id);
    List<Clube> findAll();
    List<Clube> findByAtivo(Boolean ativo);
    boolean existsByNomeAndSiglaEstado(String nome, String siglaEstado);
}
