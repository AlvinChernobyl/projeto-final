package neocamp_teamcubation.projeto_final.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.entity.Clube;
import neocamp_teamcubation.projeto_final.entity.Estadio;
import neocamp_teamcubation.projeto_final.entity.Partida;
import neocamp_teamcubation.projeto_final.repository.ClubeRepo;
import neocamp_teamcubation.projeto_final.repository.EstadioRepo;
import neocamp_teamcubation.projeto_final.repository.PartidaRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartidaService {
    private final PartidaRepo partidaRepo;
    private final EstadioRepo estadioRepo;
    private final ClubeRepo clubeRepo;

    public Partida cadastrarPartida(Partida partida) {
        if (partida.getMandante().getId() == partida.getVisitante().getId()) {
            throw new IllegalArgumentException("clubes devem ser diferentes");
        }

        Clube mandante = clubeRepo.findById(partida.getMandante().getId())
                .orElseThrow(() -> new EntityNotFoundException("mandante n encontrado"));
        Clube visitante = clubeRepo.findById(partida.getVisitante().getId())
                .orElseThrow(() -> new EntityNotFoundException("visitante n encontrado"));

        if (!mandante.getAtivo() || !visitante.getAtivo()) {
            throw new IllegalArgumentException("clubes devem estar ativos");
        }

        Estadio estadio = estadioRepo.findById(partida.getEstadio().getId())
                .orElseThrow(() -> new EntityNotFoundException("Estádio n encontrado"));

        partida.setMandante(mandante);
        partida.setVisitante(visitante);
        partida.setEstadio(estadio);

        return partidaRepo.save(partida);
    }

    public List<Partida> listarTodas() {
        return partidaRepo.findAll();
    }

    public Partida buscarPorId(Long id) {
        return partidaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("n encontrada"));
    }

    public void deletar(Long id) {
        Partida partida = partidaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("n encontrada"));

        partidaRepo.delete(partida);
    }

    public Partida atualizar(Long id, Partida nova) {
        Partida partidaExistente = partidaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("n encontrada"));

        if (nova.getMandante().getId() == nova.getVisitante().getId()) {
            throw new IllegalArgumentException("devem ser diferente");
        }

        partidaExistente.setMandante(nova.getMandante());
        partidaExistente.setVisitante(nova.getVisitante());
        partidaExistente.setEstadio(nova.getEstadio());
        partidaExistente.setDataHora(nova.getDataHora());
        partidaExistente.setGolsMandante(nova.getGolsMandante());
        partidaExistente.setGolsVisitante(nova.getGolsVisitante());

        return partidaRepo.save(partidaExistente);
    }
}
