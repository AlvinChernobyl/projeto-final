package com.mercadolivre.ex_spring_jpa.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "times")
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String pais;
    private LocalDate fundacao;

    @OneToMany(mappedBy = "time")
    private List<Atleta> atetlas;
}
