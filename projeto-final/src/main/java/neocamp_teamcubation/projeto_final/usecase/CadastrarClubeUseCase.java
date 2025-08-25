package neocamp_teamcubation.projeto_final.usecase;


import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.domain.port.in.CadastrarClubePort;
import neocamp_teamcubation.projeto_final.domain.port.out.ClubeRepositoryPort;
import neocamp_teamcubation.projeto_final.entity.Clube;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CadastrarClubeUseCase implements CadastrarClubePort {

    private final ClubeRepositoryPort repo;

    @Override
    public Clube executar(Clube c) {
        validar(c);
        if (repo.existsByNomeAndSiglaEstado(c.getNome(), c.getSiglaEstado())) {
            throw new IllegalArgumentException("Já existe clube com esse nome/UF");
        }
        if (c.getAtivo() == null) c.setAtivo(true);
        return repo.save(c);
    }

    private void validar(Clube c) {
        if (c.getNome() == null || c.getNome().trim().length() < 2)
            throw new ValidationException("Nome deve ter pelo menos 2 letras");
        if (c.getSiglaEstado() == null || !c.getSiglaEstado().matches("^[A-Z]{2}$"))
            throw new ValidationException("UF inválida (ex.: 'PA')");
        if (c.getDataCriacao() == null || c.getDataCriacao().isAfter(LocalDate.now()))
            throw new ValidationException("Data de criação obrigatória e não pode ser futura");
    }
}
