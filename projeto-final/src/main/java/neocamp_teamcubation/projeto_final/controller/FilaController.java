package neocamp_teamcubation.projeto_final.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.entity.Partida;
import neocamp_teamcubation.projeto_final.queue.FilaPartida;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fila-partida")
@RequiredArgsConstructor
public class FilaController {

    private final FilaPartida filaPartida;

    @PostMapping("/adicionar")
    public ResponseEntity<String> adicionarPartida(@RequestBody @Valid Partida partida) {
        filaPartida.adicionar(partida);
        return ResponseEntity.ok("Partida adicionada à fila com sucesso.");
    }

    @GetMapping("/consumir")
    public ResponseEntity<Partida> consumirPartida() {
        Partida partida = filaPartida.consumir();
        if (partida != null) {
            return ResponseEntity.ok(partida);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/is-vazia")
    public ResponseEntity<Boolean> isFilaVazia() {
        return ResponseEntity.ok(filaPartida.isVazia());
    }
}
