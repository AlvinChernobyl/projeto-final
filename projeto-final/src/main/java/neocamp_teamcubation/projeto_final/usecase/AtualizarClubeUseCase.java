package neocamp_teamcubation.projeto_final.usecase;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.domain.port.in.AtualizarClubePort;
import neocamp_teamcubation.projeto_final.domain.port.out.ClubeRepositoryPort;
import neocamp_teamcubation.projeto_final.entity.Clube;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AtualizarClubeUseCase implements AtualizarClubePort {

    private final ClubeRepositoryPort repo;

    @Override
    public Clube executar(Long id, Clube dados) {
        Clube existente = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Clube não encontrado"));

        validar(dados);

        boolean mudouNomeOuUf = !existente.getNome().equalsIgnoreCase(dados.getNome())
                || !existente.getSiglaEstado().equalsIgnoreCase(dados.getSiglaEstado());
        if (mudouNomeOuUf && repo.existsByNomeAndSiglaEstado(dados.getNome(), dados.getSiglaEstado())) {
            throw new IllegalArgumentException("Já existe clube com esse nome/UF");
        }

        existente.setNome(dados.getNome());
        existente.setSiglaEstado(dados.getSiglaEstado());
        existente.setDataCriacao(dados.getDataCriacao());
        existente.setLogradouro(dados.getLogradouro());
        existente.setBairro(dados.getBairro());
        existente.setCidade(dados.getCidade());
        existente.setAtivo(dados.getAtivo());

        return repo.save(existente);
    }

    private void validar(Clube c) {
        if (c.getNome() == null || c.getNome().trim().length() < 2)
            throw new ValidationException("Nome deve ter pelo menos 2 letras");
        if (c.getSiglaEstado() == null || !c.getSiglaEstado().matches("^[A-Z]{2}$"))
            throw new ValidationException("UF inválida");
        if (c.getDataCriacao() == null || c.getDataCriacao().isAfter(LocalDate.now()))
            throw new ValidationException("Data inválida");
    }
}
