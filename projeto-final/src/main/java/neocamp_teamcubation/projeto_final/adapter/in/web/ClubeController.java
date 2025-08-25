package neocamp_teamcubation.projeto_final.adapter.in.web;

import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.domain.port.in.*;
import neocamp_teamcubation.projeto_final.entity.Clube;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubes")
@RequiredArgsConstructor
public class ClubeController {

    private final CadastrarClubePort cadastrar;
    private final AtualizarClubePort atualizar;
    private final BuscarClubePort buscar;
    private final ListarClubesPort listar;
    private final InativarClubePort inativar;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Clube clube) {
        try {
            return ResponseEntity.status(201).body(cadastrar.executar(clube));
        } catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Clube novo) {
        try {
            return ResponseEntity.ok(atualizar.executar(id, novo));
        } catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> porId(@PathVariable Long id) {
        try { return ResponseEntity.ok(buscar.porId(id)); }
        catch (Exception e) { return ResponseEntity.status(404).body(e.getMessage()); }
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(required = false) Boolean ativo) {
        return ResponseEntity.ok(listar.todos(ativo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            inativar.executar(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (Exception e) { return ResponseEntity.status(404).body(e.getMessage()); }
    }
}
