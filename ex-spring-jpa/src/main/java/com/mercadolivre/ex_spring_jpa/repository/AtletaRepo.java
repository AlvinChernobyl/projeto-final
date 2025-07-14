package com.mercadolivre.ex_spring_jpa.repository;

import com.mercadolivre.ex_spring_jpa.model.Atleta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtletaRepo extends JpaRepository<Atleta, Long> {
}
