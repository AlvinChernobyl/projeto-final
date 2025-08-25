package neocamp_teamcubation.projeto_final.usecase;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.domain.port.in.CadastrarPartidaPort;
import neocamp_teamcubation.projeto_final.domain.port.out.ClubeRepositoryPort;
import neocamp_teamcubation.projeto_final.domain.port.out.EstadioRepositoryPort;
import neocamp_teamcubation.projeto_final.domain.port.out.MessagingPort;
import neocamp_teamcubation.projeto_final.domain.port.out.PartidaRepositoryPort;
import neocamp_teamcubation.projeto_final.entity.Clube;
import neocamp_teamcubation.projeto_final.entity.Estadio;
import neocamp_teamcubation.projeto_final.entity.Partida;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CadastrarPartidaUseCase implements CadastrarPartidaPort {

    private final PartidaRepositoryPort partidaRepo;
    private final ClubeRepositoryPort clubeRepo;
    private final EstadioRepositoryPort estadioRepo;
    private final MessagingPort messagingPort; // opcional: publicar evento

    @Override
    public Partida executar(Partida partida) {
        if (Objects.equals(partida.getMandante().getId(), partida.getVisitante().getId())) {
            throw new IllegalArgumentException("Mandante e visitante devem ser diferentes");
        }

        Clube mandante = clubeRepo.findById(partida.getMandante().getId())
                .orElseThrow(() -> new EntityNotFoundException("Mandante não encontrado"));
        Clube visitante = clubeRepo.findById(partida.getVisitante().getId())
                .orElseThrow(() -> new EntityNotFoundException("Visitante não encontrado"));

        if (!Boolean.TRUE.equals(mandante.getAtivo()) || !Boolean.TRUE.equals(visitante.getAtivo())) {
            throw new IllegalArgumentException("Clubes devem estar ativos");
        }

        Estadio estadio = estadioRepo.findById(partida.getEstadio().getId())
                .orElseThrow(() -> new EntityNotFoundException("Estádio não encontrado"));

        partida.setMandante(mandante);
        partida.setVisitante(visitante);
        partida.setEstadio(estadio);

        Partida salva = partidaRepo.save(partida);

        messagingPort.publicarPartidaCriada(salva);

        return salva;
    }
}
