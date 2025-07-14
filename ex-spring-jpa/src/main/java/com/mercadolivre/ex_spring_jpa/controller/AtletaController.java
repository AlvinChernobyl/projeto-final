package com.mercadolivre.ex_spring_jpa.controller;

import com.mercadolivre.ex_spring_jpa.model.Atleta;
import com.mercadolivre.ex_spring_jpa.repository.AtletaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atletas")
public class AtletaController {

    @Autowired
    private AtletaRepo atletaRepo;

    @GetMapping
    public List<Atleta> listar() {
        return atletaRepo.findAll();
    }

    @PostMapping
    public Atleta salvar(@RequestBody Atleta atleta) {
        return atletaRepo.save(atleta);
    }
}
