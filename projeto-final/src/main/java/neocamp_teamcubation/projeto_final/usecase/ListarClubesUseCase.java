package neocamp_teamcubation.projeto_final.usecase;


import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.domain.port.in.ListarClubesPort;
import neocamp_teamcubation.projeto_final.domain.port.out.ClubeRepositoryPort;
import neocamp_teamcubation.projeto_final.entity.Clube;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarClubesUseCase implements ListarClubesPort {

    private final ClubeRepositoryPort repo;

    @Override
    public List<Clube> todos(Boolean ativo) {
        return ativo == null ? repo.findAll() : repo.findByAtivo(ativo);
    }
}
