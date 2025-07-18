package neocamp_teamcubation.projeto_final.service;

import jakarta.persistence.EntityNotFoundException;
import neocamp_teamcubation.projeto_final.entity.Clube;
import neocamp_teamcubation.projeto_final.entity.Estadio;
import neocamp_teamcubation.projeto_final.entity.Partida;
import neocamp_teamcubation.projeto_final.repository.ClubeRepo;
import neocamp_teamcubation.projeto_final.repository.EstadioRepo;
import neocamp_teamcubation.projeto_final.repository.PartidaRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PartidaServiceTest {

    @Mock
    private PartidaRepo partidaRepo;

    @Mock
    private ClubeRepo clubeRepo;

    @Mock
    private EstadioRepo estadioRepo;

    @InjectMocks
    private PartidaService partidaService;

    public PartidaServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void cadastrarPartidaSucesso() {
        Clube mandante = new Clube();
        mandante.setId(1L);
        mandante.setAtivo(true);

        Clube visitante = new Clube();
        visitante.setId(2L);
        visitante.setAtivo(true);

        Estadio estadio = new Estadio();
        estadio.setId(1L);

        Partida partida = new Partida();
        partida.setMandante(mandante);
        partida.setVisitante(visitante);
        partida.setEstadio(estadio);
        partida.setDataHora(LocalDateTime.now());
        partida.setGolsMandante(1);
        partida.setGolsVisitante(2);

        when(clubeRepo.findById(1L)).thenReturn(Optional.of(mandante));
        when(clubeRepo.findById(2L)).thenReturn(Optional.of(visitante));
        when(estadioRepo.findById(1L)).thenReturn(Optional.of(estadio));
        when(partidaRepo.save(any())).thenReturn(partida);

        Partida resultado = partidaService.cadastrarPartida(partida);

        assertNotNull(resultado);
        assertEquals(1, resultado.getGolsMandante());
        assertEquals(2, resultado.getGolsVisitante());
    }

    @Test
    public void falharSeEstadioNaoExiste() {
        Clube mandante = new Clube();
        mandante.setId(1L);
        mandante.setAtivo(true);

        Clube visitante = new Clube();
        visitante.setId(2L);
        visitante.setAtivo(true);

        Partida partida = new Partida();
        partida.setMandante(mandante);
        partida.setVisitante(visitante);
        partida.setEstadio(new Estadio());
        partida.getEstadio().setId(99L);
        partida.setDataHora(LocalDateTime.now());

        when(clubeRepo.findById(1L)).thenReturn(Optional.of(mandante));
        when(clubeRepo.findById(2L)).thenReturn(Optional.of(visitante));
        when(estadioRepo.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            partidaService.cadastrarPartida(partida);
        });

        assertEquals("Estádio n encontrado", exception.getMessage());
    }

    @Test
    public void buscarPartidaPorId() {
        Partida partida = new Partida();
        partida.setId(1L);

        when(partidaRepo.findById(1L)).thenReturn(Optional.of(partida));

        Partida resultado = partidaService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    public void falharPartidaInexistente() {
        when(partidaRepo.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            partidaService.buscarPorId(999L);
        });

        assertEquals("n encontrada", exception.getMessage());
    }

    @Test
    public void atualizarPartida() {
        Partida antiga = new Partida();
        antiga.setId(1L);
        antiga.setGolsMandante(0);
        antiga.setGolsVisitante(0);

        Partida nova = new Partida();
        nova.setMandante(new Clube());
        nova.getMandante().setId(1L);
        nova.setVisitante(new Clube());
        nova.getVisitante().setId(2L);
        nova.setEstadio(new Estadio());
        nova.getEstadio().setId(1L);
        nova.setDataHora(LocalDateTime.now());
        nova.setGolsMandante(2);
        nova.setGolsVisitante(1);

        when(partidaRepo.findById(1L)).thenReturn(Optional.of(antiga));
        when(partidaRepo.save(any())).thenReturn(nova);

        Partida atualizada = partidaService.atualizar(1L, nova);

        assertEquals(2, atualizada.getGolsMandante());
        assertEquals(1, atualizada.getGolsVisitante());
    }

    @Test
    public void falharAoAtualizarPartidaInexistente() {
        Partida nova = new Partida();
        when(partidaRepo.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            partidaService.atualizar(999L, nova);
        });

        assertEquals("n encontrada", exception.getMessage());
    }

    public void apagar(Long id) {
        Partida partida = partidaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada"));
        partidaRepo.delete(partida);
    }

    @Test
    public void removerPartida() {
        Partida partida = new Partida();
        partida.setId(1L);

        when(partidaRepo.findById(1L)).thenReturn(Optional.of(partida));
        doNothing().when(partidaRepo).delete(partida);

        assertDoesNotThrow(() -> partidaService.deletar(1L));

        verify(partidaRepo, times(1)).delete(partida);
    }

    @Test
    public void falharRemoverPartidaInexistente() {
        when(partidaRepo.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            partidaService.deletar(999L);
        });

        assertEquals("n encontrada", exception.getMessage());
    }
}