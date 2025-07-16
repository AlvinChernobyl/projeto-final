package neocamp_teamcubation.projeto_final.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.entity.Clube;
import neocamp_teamcubation.projeto_final.repository.ClubeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubeService {

    private final ClubeRepository clubeRepository;

    public List<Clube> clubeList(String nome) {
        if (nome == null || nome.isBlank()) {
            return clubeRepository.findAll();
        }
        return clubeRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Clube cadastrarClube(Clube clube) {
        validateClube(clube);

        boolean jaExiste = clubeRepository.existsByNomeAndSiglaEstado(clube.getNome(), clube.getSiglaEstado());

        if (jaExiste) {
            throw new IllegalArgumentException("Ja existe um clube com o nome " + clube.getNome());
        }

        return clubeRepository.save(clube);
    }

    public Clube buscarId(Long id) {
        return clubeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Clube náo encontrado"));
    }

    private void validateClube(Clube clube) {
        if (clube.getNome() == null || clube.getNome().length() < 2) {
            throw new ValidationException("nome deve ter duas letras");
        }

        if (clube.getSiglaEstado() == null || !clube.getSiglaEstado().matches("^[A-Z]{2}$")) {
            throw new ValidationException("sigla do estado inválida. ex: 'SP'");
        }

        if (clube.getDataCriacao() != null && clube.getDataCriacao().isAfter(LocalDate.now())) {
            throw new ValidationException("data de criacao invalida");
        }
    }
}
