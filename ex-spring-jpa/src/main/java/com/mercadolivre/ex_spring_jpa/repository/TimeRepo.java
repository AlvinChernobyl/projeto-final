package com.mercadolivre.ex_spring_jpa.repository;

import com.mercadolivre.ex_spring_jpa.model.Time;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRepo extends JpaRepository<Time,Long> {
}
