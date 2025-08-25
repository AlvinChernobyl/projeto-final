package neocamp_teamcubation.projeto_final.usecase;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.domain.port.in.BuscarClubePort;
import neocamp_teamcubation.projeto_final.domain.port.out.ClubeRepositoryPort;
import neocamp_teamcubation.projeto_final.entity.Clube;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarClubeUseCase implements BuscarClubePort {

    private final ClubeRepositoryPort repo;

    @Override
    public Clube porId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Clube não encontrado"));
    }
}
