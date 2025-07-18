package neocamp_teamcubation.projeto_final.service;
import jakarta.persistence.EntityNotFoundException;
import neocamp_teamcubation.projeto_final.entity.Estadio;
import neocamp_teamcubation.projeto_final.repository.EstadioRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EstadioServiceTest {

    @Mock
    private EstadioRepo estadioRepo;

    @InjectMocks
    private EstadioService estadioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarEstadioComSucesso() {
        Estadio estadio = new Estadio();
        estadio.setNome("Mangueirão");

        when(estadioRepo.existsByNome("Mangueirão")).thenReturn(false);
        when(estadioRepo.save(any())).thenReturn(estadio);

        Estadio salvo = estadioService.cadastrarEstadio(estadio);

        assertNotNull(salvo);
        assertEquals("Mangueirão", salvo.getNome());
    }

    @Test
    void falharAoCadastrarPorCaracter() {
        Estadio estadio = new Estadio();
        estadio.setNome("Be");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> estadioService.cadastrarEstadio(estadio));
        assertTrue(ex.getMessage().contains("no minimo 3 caracteres"));
    }

    @Test
    void falharJaExistente() {
        Estadio estadio = new Estadio();
        estadio.setNome("Mangueirão");

        when(estadioRepo.existsByNome("Mangueirão")).thenReturn(true);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> estadioService.cadastrarEstadio(estadio));
        assertTrue(ex.getMessage().contains("Já existe um estádio"));
    }

    @Test
    void buscarEstadioPorId() {
        Estadio estadio = new Estadio();
        estadio.setId(1L);

        when(estadioRepo.findById(1L)).thenReturn(Optional.of(estadio));

        Estadio resultado = estadioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void falharEstadioInexistente() {
        when(estadioRepo.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(EntityNotFoundException.class, () -> estadioService.buscarPorId(99L));
        assertTrue(ex.getMessage().contains("Estadio foud"));
    }
}