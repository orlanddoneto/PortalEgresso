package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Curso_Egresso;
import com.muxegresso.egresso.domain.Egresso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Curso_EgressoRepository extends JpaRepository<Curso_Egresso, Integer> {
    @Query("SELECT ce.curso FROM Curso_Egresso ce WHERE ce.egresso.id = :egressoId")
    Page<Curso> findCursosByEgressoId(@Param("egressoId") Integer egressoId, Pageable pageable);

    @Query("SELECT ce.egresso FROM Curso_Egresso ce WHERE ce.curso.id = :cursoId")
    Page<Egresso> findEgressosByCursoId(@Param("cursoId") Integer cursoId, Pageable pageable);


}
