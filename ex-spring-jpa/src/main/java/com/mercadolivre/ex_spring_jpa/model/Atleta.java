package com.mercadolivre.ex_spring_jpa.model;


import jakarta.persistence.*;

@Entity
@Table(name = "atletas")
public class Atleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String posicao;

    @ManyToOne
    @JoinColumn(name = "time_id")
    private Time time;




}
