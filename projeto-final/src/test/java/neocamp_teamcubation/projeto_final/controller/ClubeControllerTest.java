package neocamp_teamcubation.projeto_final.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import neocamp_teamcubation.projeto_final.entity.Clube;
import neocamp_teamcubation.projeto_final.service.ClubeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClubeController.class)
class ClubeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClubeService clubeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Clube clube;

    @BeforeEach
    void setUp() {
        clube = new Clube();
        clube.setId(1L);
        clube.setNome("Palmeiras");
        clube.setSiglaEstado("SP");
        clube.setDataCriacao(LocalDate.of(1914, 8, 26));
        clube.setAtivo(true);
    }

    @Test
    void deveListarClubes() throws Exception {
        Mockito.when(clubeService.clubeList(null)).thenReturn(List.of(clube));

        mockMvc.perform(get("/clubes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Palmeiras"));
    }

    @Test
    void deveBuscarClubePorId() throws Exception {
        Mockito.when(clubeService.buscarId(1L)).thenReturn(clube);

        mockMvc.perform(get("/clubes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Palmeiras"));
    }

    @Test
    void deveRetornar404SeNaoEncontrarClube() throws Exception {
        Mockito.when(clubeService.buscarId(99L)).thenThrow(new jakarta.persistence.EntityNotFoundException("Clube não encontrado"));

        mockMvc.perform(get("/clubes/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Clube não encontrado"));
    }

    @Test
    void deveCadastrarClube() throws Exception {
        Mockito.when(clubeService.cadastrarClube(any(Clube.class))).thenReturn(clube);

        mockMvc.perform(post("/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clube)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Palmeiras"));
    }

    @Test
    void deveRetornar409AoCadastrarDuplicado() throws Exception {
        Mockito.when(clubeService.cadastrarClube(any(Clube.class)))
                .thenThrow(new IllegalArgumentException("Ja existe um clube com o nome Palmeiras"));

        mockMvc.perform(post("/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clube)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Ja existe um clube com o nome Palmeiras"));
    }

    @Test
    void deveEditarClube() throws Exception {
        Clube atualizado = new Clube();
        atualizado.setId(1L);
        atualizado.setNome("Palmeiras FC");
        atualizado.setSiglaEstado("SP");
        atualizado.setDataCriacao(LocalDate.of(1914, 8, 26));
        atualizado.setAtivo(true);

        Mockito.when(clubeService.editarClube(eq(1L), any(Clube.class))).thenReturn(atualizado);

        mockMvc.perform(put("/clubes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Palmeiras FC"));
    }

    @Test
    void deveExcluirClube() throws Exception {
        Mockito.doNothing().when(clubeService).apagarClube(1L);

        mockMvc.perform(delete("/clubes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoExcluirInexistente() throws Exception {
        Mockito.doThrow(new jakarta.persistence.EntityNotFoundException("n encontrado"))
                .when(clubeService).apagarClube(99L);

        mockMvc.perform(delete("/clubes/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("n encontrado"));
    }
}