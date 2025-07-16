package neocamp_teamcubation.projeto_final.repository;

import neocamp_teamcubation.projeto_final.entity.Clube;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubeRepository extends CrudRepository<Clube, Long> {

    Optional<Clube> findByNomeAndSiglaEstado(String nome, String siglaEstado);

    boolean existsByNomeAndSiglaEstado(String nome, String siglaEstado);
}
