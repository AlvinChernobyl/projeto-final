package neocamp_teamcubation.projeto_final.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.entity.Clube;
import neocamp_teamcubation.projeto_final.entity.Estadio;
import neocamp_teamcubation.projeto_final.entity.Partida;
import neocamp_teamcubation.projeto_final.adapter.out.persistence.springdata.ClubeRepo;
import neocamp_teamcubation.projeto_final.adapter.out.persistence.springdata.EstadioRepo;
import neocamp_teamcubation.projeto_final.adapter.out.persistence.springdata.PartidaRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartidaService {
    private final PartidaRepo partidaRepo;
    private final EstadioRepo estadioRepo;
    private final ClubeRepo clubeRepo;
    private final KafkaProducerService kafkaProducerService;
    private final ObjectMapper objectMapper;

    public Partida criarEPublicar(Partida partida) {
        Partida salva = salvarSomente(partida);
        try {
            String json = objectMapper.writeValueAsString(salva);
            kafkaProducerService.enviarMensagem("partidas", json);
        } catch (JsonProcessingException e) {
            System.err.println("Erro ao serializar partida para Kafka: " + e.getMessage());
        }
        return salva;
    }

    public Partida salvarSomente(Partida partida) {
        if (Objects.equals(partida.getMandante().getId(), partida.getVisitante().getId())) {
            throw new IllegalArgumentException("Mandante e visitante devem ser diferentes");
        }

        Clube mandante = clubeRepo.findById(partida.getMandante().getId())
                .orElseThrow(() -> new EntityNotFoundException("Mandante não encontrado"));
        Clube visitante = clubeRepo.findById(partida.getVisitante().getId())
                .orElseThrow(() -> new EntityNotFoundException("Visitante não encontrado"));

        if (!mandante.getAtivo() || !visitante.getAtivo()) {
            throw new IllegalArgumentException("Clubes devem estar ativos");
        }

        Estadio estadio = estadioRepo.findById(partida.getEstadio().getId())
                .orElseThrow(() -> new EntityNotFoundException("Estádio não encontrado"));

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
                .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada"));
    }

    public void deletar(Long id) {
        Partida partida = partidaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada"));
        partidaRepo.delete(partida);
    }

    public Partida atualizar(Long id, Partida nova) {
        Partida existente = partidaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada"));

        if (Objects.equals(nova.getMandante().getId(), nova.getVisitante().getId())) {
            throw new IllegalArgumentException("Mandante e visitante devem ser diferentes");
        }

        Clube mandante = clubeRepo.findById(nova.getMandante().getId())
                .orElseThrow(() -> new EntityNotFoundException("Mandante não encontrado"));
        Clube visitante = clubeRepo.findById(nova.getVisitante().getId())
                .orElseThrow(() -> new EntityNotFoundException("Visitante não encontrado"));
        Estadio estadio = estadioRepo.findById(nova.getEstadio().getId())
                .orElseThrow(() -> new EntityNotFoundException("Estádio não encontrado"));

        existente.setMandante(mandante);
        existente.setVisitante(visitante);
        existente.setEstadio(estadio);
        existente.setDataHora(nova.getDataHora());
        existente.setGolsMandante(nova.getGolsMandante());
        existente.setGolsVisitante(nova.getGolsVisitante());

        return partidaRepo.save(existente);
    }

    public List<Partida> buscarComFiltros(
            String nomeMandante,
            String nomeVisitante,
            Long estadioId,
            LocalDate dataInicial,
            LocalDate dataFinal,
            Boolean futuro) {

        List<Partida> todas = partidaRepo.findAll();

        return todas.stream()
                .filter(p -> nomeMandante == null
                        || p.getMandante().getNome().toLowerCase().contains(nomeMandante.toLowerCase()))
                .filter(p -> nomeVisitante == null
                        || p.getVisitante().getNome().toLowerCase().contains(nomeVisitante.toLowerCase()))
                .filter(p -> estadioId == null
                        || Objects.equals(p.getEstadio().getId(), estadioId))
                .filter(p -> {
                    if (dataInicial != null && dataFinal != null) {
                        return !p.getDataHora().toLocalDate().isBefore(dataInicial)
                                && !p.getDataHora().toLocalDate().isAfter(dataFinal);
                    }
                    if (dataInicial != null) {
                        return !p.getDataHora().toLocalDate().isBefore(dataInicial);
                    }
                    if (dataFinal != null) {
                        return !p.getDataHora().toLocalDate().isAfter(dataFinal);
                    }
                    return true;
                })
                .filter(p -> futuro == null
                        || (futuro && p.getDataHora().isAfter(LocalDateTime.now())))
                .collect(Collectors.toList());
    }


}
