package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Egresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EgressoRepository extends JpaRepository<Egresso, Integer>, JpaSpecificationExecutor<Egresso> {

    Optional<Egresso> findByCpf(String cpf);

    boolean existsByNome(String nome);

    boolean existsByEmail(String email);

    Optional<Egresso> findByEmail(String email);

    boolean existsByCpf(String cpf);

    /*
    @Query("SELECT e FROM Egresso e JOIN e.cargos ec WHERE ec.cargo.id = :cargoId")
    List<Egresso> findAllByCargo(@Param("cargoId") Integer cargoId);

    @Query("SELECT e FROM Egresso e JOIN e.egressoCursos ce WHERE ce.curso.id = :cursoId")
    List<Egresso> findAllByCurso(@Param("cursoId") Integer cursoId);

     */
}