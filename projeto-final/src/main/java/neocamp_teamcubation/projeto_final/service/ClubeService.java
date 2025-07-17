package neocamp_teamcubation.projeto_final.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.entity.Clube;
import neocamp_teamcubation.projeto_final.repository.ClubeRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubeService {

    private final ClubeRepo clubeRepo;

    public List<Clube> clubeList(String nome) {
        if (nome == null || nome.isBlank()) {
            return clubeRepo.findByAtivoTrue();
        }
        return clubeRepo.findByNomeContainingIgnoreCase(nome);
    }

    public Clube cadastrarClube(Clube clube) {
        validateClube(clube);

        boolean jaExiste = clubeRepo.existsByNomeAndSiglaEstado(clube.getNome(), clube.getSiglaEstado());

        if (jaExiste) {
            throw new IllegalArgumentException("Ja existe um clube com o nome " + clube.getNome());
        }

        return clubeRepo.save(clube);
    }

    public Clube buscarId(Long id) {
        return clubeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Clube náo encontrado"));
    }

    private void validateClube(Clube clube) {
        if (clube.getNome() == null || clube.getNome().length() < 2) {
            throw new IllegalArgumentException("nome deve ter duas letras");
        }

        if (clube.getSiglaEstado() == null || !clube.getSiglaEstado().matches("^[A-Z]{2}$")) {
            throw new IllegalArgumentException("sigla do estado inválida. ex: 'SP'");
        }

        if (clube.getDataCriacao() != null && clube.getDataCriacao().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("data de criacao invalida");
        }
    }

    public Clube editarClube(Long id, Clube novoClube) {
        Clube clubeExistente = clubeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("não encontrado"));

        validateClube(novoClube);

        boolean conflito = clubeRepo.existsByNomeAndSiglaEstado(novoClube.getNome(), novoClube.getSiglaEstado());

        if (conflito && (!novoClube.getNome().equals(clubeExistente.getNome()) || !novoClube.getSiglaEstado().equals(clubeExistente.getSiglaEstado()))) {
            throw new IllegalArgumentException("nome e estado ja existente");
        }

        clubeExistente.setNome(novoClube.getNome());
        clubeExistente.setSiglaEstado(novoClube.getSiglaEstado());
        clubeExistente.setDataCriacao(novoClube.getDataCriacao());

        return clubeRepo.save(clubeExistente);
    }

    public void apagarClube(Long id) {
        Clube clube = clubeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("n encontrado"));
        if (Boolean.FALSE.equals(clube.getAtivo())) {
            return;
        }
        clube.setAtivo(false);
        clubeRepo.save(clube);
    }


}
