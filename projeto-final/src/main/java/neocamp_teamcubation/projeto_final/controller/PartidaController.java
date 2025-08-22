package neocamp_teamcubation.projeto_final.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.DTO.ViaCepDTO;
import neocamp_teamcubation.projeto_final.entity.Clube;
import neocamp_teamcubation.projeto_final.entity.Partida;
import neocamp_teamcubation.projeto_final.service.ClubeService;
import neocamp_teamcubation.projeto_final.service.PartidaService;
import neocamp_teamcubation.projeto_final.service.ViaCepService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/partidas")
@RequiredArgsConstructor
public class PartidaController {

    private final PartidaService partidaService;
    private final ViaCepService viaCepService;
    private  final ClubeService clubeService;

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(partidaService.buscarPorId(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Partida não encontrada");
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodas() {
        return ResponseEntity.ok(partidaService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Partida partida) {
        try {
            Partida nova = partidaService.criarEPublicar(partida);
            return ResponseEntity.status(201).body(nova);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            partidaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("n encontrada");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Partida novaPartida) {
        try {
            Partida atualizada = partidaService.atualizar(id, novaPartida);
            return ResponseEntity.ok(atualizada);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Partida>> buscarComFiltro(
            @RequestParam(required = false) String nomeMandante,
            @RequestParam(required = false) String nomeVisitante,
            @RequestParam(required = false) Long estadioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam(required = false) Boolean futuro
    ) {
        List<Partida> partidas = partidaService.buscarComFiltros(
                nomeMandante, nomeVisitante, estadioId, dataInicial, dataFinal, futuro);

        return ResponseEntity.ok(partidas);
    }

    @PostMapping("/cep")
    public ResponseEntity<?> cadastrarPorCep(@RequestParam String cep) {
        ViaCepDTO dados = viaCepService.consultarCep(cep);

        Clube clube = new Clube();
        clube.setNome("Clube " + dados.getLocalidade());
        clube.setSiglaEstado(dados.getUf());
        clube.setAtivo(true);

        Clube salvo = clubeService.cadastrarClube(clube);
        return ResponseEntity.ok(salvo);
    }
}
