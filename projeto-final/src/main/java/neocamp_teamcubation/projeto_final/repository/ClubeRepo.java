package neocamp_teamcubation.projeto_final.repository;

import neocamp_teamcubation.projeto_final.entity.Clube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubeRepo extends JpaRepository<Clube, Long> {

    List<Clube> findByNomeContainingIgnoreCase(String nome);
    List<Clube> findByAtivoTrue();

    boolean existsByNomeAndSiglaEstado(String nome, String siglaEstado);
}
