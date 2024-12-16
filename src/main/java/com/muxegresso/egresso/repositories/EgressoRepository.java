package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Egresso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EgressoRepository extends JpaRepository<Egresso,Integer> {
    Optional<Egresso> FindByEmail(String email);

}
