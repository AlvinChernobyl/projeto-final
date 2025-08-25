package neocamp_teamcubation.projeto_final.adapter.out.persistence.springdata;


import neocamp_teamcubation.projeto_final.entity.Clube;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubeRepo extends JpaRepository<Clube, Long> {
    boolean existsByNomeAndSiglaEstado(String nome, String siglaEstado);
    List<Clube> findByAtivo(Boolean ativo);
}
