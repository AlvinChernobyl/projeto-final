package neocamp_teamcubation.projeto_final.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.entity.Estadio;
import neocamp_teamcubation.projeto_final.repository.EstadioRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadioService {

    private final EstadioRepo estadioRepo;

    public Estadio cadastrarEstadio(Estadio estadio) {
        if (estadio.getNome() == null || estadio.getNome().length() < 3) {
            throw new IllegalArgumentException("O nome deve ter no minimo 3 caracteres");
        }

        if (estadioRepo.existsByNome(estadio.getNome())) {
            throw new IllegalArgumentException("Já existe um estádio com esse nome.");
        }
        return estadioRepo.save(estadio);
    }

    public Estadio buscarPorId(Long id) {
        return estadioRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Estadio foud"));
    }

    public List<Estadio> buscarTodos() {
        return estadioRepo.findAll();
    }
}
