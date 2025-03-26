package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Cargo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CargoRepository extends JpaRepository<Cargo,Integer> {

    @Query("SELECT c FROM Cargo c ORDER BY c.salario DESC")
    List<Cargo> findTopSalarios(Pageable pageable);
}
