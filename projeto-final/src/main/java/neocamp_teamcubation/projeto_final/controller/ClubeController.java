package neocamp_teamcubation.projeto_final.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.entity.Clube;
import neocamp_teamcubation.projeto_final.service.ClubeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clubes")
@RequiredArgsConstructor
public class ClubeController {

    private final ClubeService clubeService;

    @GetMapping
    public ResponseEntity<List<Clube>> listarClubes(@RequestParam(required = false) String nome) {
        List<Clube> clubes = clubeService.clubeList(nome);
        return ResponseEntity.ok(clubes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id) {
        try {
            Clube clube = clubeService.buscarId(id);
            return ResponseEntity.ok(clube);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid Clube clube) {
        try {
            Clube clubeSalvo = clubeService.cadastrarClube(clube);
            return ResponseEntity.status(201).body(clubeSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody @Valid Clube clube) {
        try {
            Clube atualizado = clubeService.editarClube(id, clube);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            clubeService.apagarClube(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
