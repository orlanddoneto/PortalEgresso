package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso,Integer> {
}
