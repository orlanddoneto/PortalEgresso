package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Coordenador;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.domain.Egresso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoordenadorRepository extends JpaRepository<Coordenador,Integer> {
    Optional<Coordenador> findByEmail(String email);


    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer id);

    Page<Coordenador> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
