package neocamp_teamcubation.projeto_final.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.entity.Clube;
import neocamp_teamcubation.projeto_final.repository.ClubeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ClubeService {

    private final ClubeRepository clubeRepository;
    private final LocaleResolver localeResolver;

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
