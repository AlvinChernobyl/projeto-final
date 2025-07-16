package neocamp_teamcubation.projeto_final.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.entity.Clube;
import neocamp_teamcubation.projeto_final.repository.ClubeRepository;
import neocamp_teamcubation.projeto_final.service.ClubeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clubes")
@RequiredArgsConstructor
public class ClubeController {

    private final ClubeService clubeService;

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid Clube clube) {
        try {
            Clube clubeSalvo = clubeService.cadastrarClube(clube);
            return ResponseEntity.status(201).body(clubeSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
