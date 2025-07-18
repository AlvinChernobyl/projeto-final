package neocamp_teamcubation.projeto_final.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.entity.Estadio;
import neocamp_teamcubation.projeto_final.service.EstadioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estadios")
@RequiredArgsConstructor
public class EstadioController {

    private final EstadioService estadioService;

    @GetMapping
    public ResponseEntity<List<Estadio>> buscarTodos() {
        List<Estadio> lista = estadioService.buscarTodos();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid Estadio estadio) {
        try {
            Estadio salvo = estadioService.cadastrarEstadio(estadio);
            return ResponseEntity.ok(salvo);

        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Estadio estadio = estadioService.buscarPorId(id);
            return ResponseEntity.ok(estadio);

        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
