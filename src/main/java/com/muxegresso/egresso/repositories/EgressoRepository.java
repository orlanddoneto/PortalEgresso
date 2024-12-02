package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Egresso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EgressoRepository extends JpaRepository<Egresso,Integer> {
}
