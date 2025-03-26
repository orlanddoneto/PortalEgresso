package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Egresso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso,Integer> {
    Page<Curso> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    @Query("SELECT c.nome, COUNT(ce.egresso.id) " +
            "FROM Curso c JOIN c.cursoEgressos ce " +
            "GROUP BY c.nome " +
            "ORDER BY COUNT(ce.egresso.id) DESC")
    List<Object[]> contarEgressosPorCurso();
}
