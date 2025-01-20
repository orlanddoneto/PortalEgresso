package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Egresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface EgressoRepository extends JpaRepository<Egresso, Integer>, JpaSpecificationExecutor<Egresso> {

    Optional<Egresso> findByCpf(String cpf);

    boolean existsByNome(String nome);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Optional<Egresso> findByCurso(Curso curso);

    Egresso findByCargo(Cargo cargo);

    List<Egresso> findAllByCargo();
}