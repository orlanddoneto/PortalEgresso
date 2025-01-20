package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Coordenador;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.domain.Egresso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoordenadorRepository extends JpaRepository<Coordenador,Integer> {
    Optional<Coordenador> FindByEmail(String email);

    Optional<Coordenador> findByLogin(String login);
}
