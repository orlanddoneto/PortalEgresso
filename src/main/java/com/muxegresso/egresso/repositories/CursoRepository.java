package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Egresso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso,Integer> {
    Page<Curso> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
