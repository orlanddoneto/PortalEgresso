package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.dtos.RequestDepoimentoDto;
import com.muxegresso.egresso.domain.enums.AproveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface DepoimentoRepository extends JpaRepository<Depoimento,Integer> {
    @Query(value = "SELECT * FROM tb_depoimento d WHERE EXTRACT(YEAR FROM d.data) = :ano", nativeQuery = true)
    Page<Depoimento> findAllByAno(@Param("ano") int ano, Pageable pageable);


    @Query(value = "SELECT * FROM tb_depoimento WHERE data >= CURRENT_DATE - CAST(:dias || ' days' AS INTERVAL)", nativeQuery = true)
    Page<Depoimento> findDepoimentosRecentes(@Param("dias") int dias, Pageable pageable);


    Page<Depoimento> findAllByEgressoId(Pageable page, Integer id);

    Page<Depoimento> findByHomologadoStatus(AproveStatus aproveStatus, Pageable pageable);

    Page<Depoimento> findByHomologadoStatusAndEgressoId(AproveStatus aproveStatus,Integer egressoId, Pageable pageable);

    Page<Depoimento> findByHomologadoStatusAndEgressoIdAndTextoContainingIgnoreCase(
            AproveStatus status, Integer egressoId, String texto, Pageable pageable);

    Page<Depoimento> findByHomologadoStatusAndTextoContainingIgnoreCase(
            AproveStatus status, String texto, Pageable pageable);
}

