package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.Oportunidade;
import com.muxegresso.egresso.domain.dtos.SendOportunidadeDto;
import com.muxegresso.egresso.domain.enums.AproveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OportunidadeRepository extends JpaRepository<Oportunidade, Integer>, JpaSpecificationExecutor<Oportunidade> {
    @Query(value = "SELECT * FROM tb_oportunidade o WHERE EXTRACT(YEAR FROM o.created_at) = :ano", nativeQuery = true)
    Page<Oportunidade> findAllByAno(@Param("ano") int ano, Pageable pageable);


    @Query(value = "SELECT * FROM tb_oportunidade WHERE created_at >= CURRENT_DATE - CAST(:dias || ' days' AS INTERVAL)", nativeQuery = true)
    Page<Oportunidade> findOportunidadesRecentes(@Param("dias") int dias, Pageable pageable);

    Page<Oportunidade> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);

    Page<Oportunidade> findByHomologadoStatus(AproveStatus aproveStatus, Pageable pageable);

    Page<Oportunidade> findByHomologadoStatusAndEgressoId(AproveStatus aproveStatus, Integer egressoId, Pageable pageable);

    Page<Oportunidade> findAllByEgressoId(Pageable pageable, Integer id);

    Page<Oportunidade> findByHomologadoStatusAndEgressoIdAndTituloContainingIgnoreCase(
            AproveStatus status, Integer egressoId, String titulo, Pageable pageable);

    Page<Oportunidade> findByHomologadoStatusAndTituloContainingIgnoreCase(
            AproveStatus status, String titulo, Pageable pageable);
}
