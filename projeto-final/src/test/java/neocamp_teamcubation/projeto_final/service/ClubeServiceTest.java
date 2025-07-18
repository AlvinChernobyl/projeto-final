package neocamp_teamcubation.projeto_final.service;

import jakarta.persistence.EntityNotFoundException;
import neocamp_teamcubation.projeto_final.entity.Clube;
import neocamp_teamcubation.projeto_final.repository.ClubeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ClubeServiceTest {

    @Mock
    private ClubeRepo clubeRepo;

    @InjectMocks
    private ClubeService clubeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarClubesNome() {
        Clube clube = new Clube();
        clube.setNome("Remo");
        when(clubeRepo.findByNomeContainingIgnoreCase("Remo")).thenReturn(List.of(clube));

        List<Clube> resultado = clubeService.clubeList("Remo");

        assertEquals(1, resultado.size());
        assertEquals("Remo", resultado.get(0).getNome());
    }

    @Test
    void listarClubesAtivosNomeNulo() {
        Clube clube = new Clube();
        clube.setAtivo(true);
        when(clubeRepo.findByAtivoTrue()).thenReturn(List.of(clube));

        List<Clube> resultado = clubeService.clubeList(null);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getAtivo());
    }

    @Test
    void cadastrarClubeComExito() {
        Clube clube = new Clube();
        clube.setNome("Paysandu");
        clube.setSiglaEstado("PA");
        clube.setDataCriacao(LocalDate.of(1914, 2, 2));

        when(clubeRepo.existsByNomeAndSiglaEstado("Paysandu", "PA")).thenReturn(false);
        when(clubeRepo.save(any())).thenReturn(clube);

        Clube salvo = clubeService.cadastrarClube(clube);

        assertNotNull(salvo);
        assertEquals("Paysandu", salvo.getNome());
    }

    @Test
    void falharAoCadastrarDuplicado() {
        Clube clube = new Clube();
        clube.setNome("Paysandu");
        clube.setSiglaEstado("PA");

        when(clubeRepo.existsByNomeAndSiglaEstado("Paysandu", "PA")).thenReturn(true);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> clubeService.cadastrarClube(clube));
        assertTrue(ex.getMessage().contains("Ja existe um clube com o nome"));
    }

    @Test
    void clubePorIdBusca() {
        Clube clube = new Clube();
        clube.setId(1L);

        when(clubeRepo.findById(1L)).thenReturn(Optional.of(clube));

        Clube resultado = clubeService.buscarId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void clubeInexistente() {
        when(clubeRepo.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(EntityNotFoundException.class, () -> clubeService.buscarId(99L));
        assertFalse(ex.getMessage().contains("n encontrado"));
    }

    @Test
    void editarClube() {
        Clube existente = new Clube();
        existente.setId(1L);
        existente.setNome("Paysandu");
        existente.setSiglaEstado("PA");

        Clube novo = new Clube();
        novo.setNome("Paysamdu FC");
        novo.setSiglaEstado("PA");
        novo.setDataCriacao(LocalDate.of(1914, 2, 2));

        when(clubeRepo.findById(1L)).thenReturn(Optional.of(existente));
        when(clubeRepo.existsByNomeAndSiglaEstado("Paysamdu FC", "PA")).thenReturn(false);
        when(clubeRepo.save(any())).thenReturn(novo);

        Clube atualizado = clubeService.editarClube(1L, novo);

        assertEquals("Paysamdu FC", atualizado.getNome());
    }

    @Test
    void falharAoEditar() {
        Clube novo = new Clube();
        when(clubeRepo.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(EntityNotFoundException.class, () -> clubeService.editarClube(99L, novo));
        assertTrue(ex.getMessage().contains("não encontrado"));
    }

    @Test
    void apagarClube() {
        Clube clube = new Clube();
        clube.setId(1L);
        clube.setAtivo(true);

        when(clubeRepo.findById(1L)).thenReturn(Optional.of(clube));
        when(clubeRepo.save(any())).thenReturn(clube);

        assertDoesNotThrow(() -> clubeService.apagarClube(1L));
        assertFalse(clube.getAtivo());
    }

    @Test
    void apagarClubeInexistente() {
        when(clubeRepo.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(EntityNotFoundException.class, () -> clubeService.apagarClube(99L));
        assertTrue(ex.getMessage().contains("n encontrado"));
    }

    @Test
    void naoApagarClubeJaInativo() {
        Clube clube = new Clube();
        clube.setId(1L);
        clube.setAtivo(false);

        when(clubeRepo.findById(1L)).thenReturn(Optional.of(clube));

        assertDoesNotThrow(() -> clubeService.apagarClube(1L));
        verify(clubeRepo, never()).save(clube);
    }
}