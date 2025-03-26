package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.enums.AproveStatus;
import com.muxegresso.egresso.domain.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT c FROM Egresso e JOIN e.cargos c WHERE e.id = :id")
    Page<Cargo> findCargosById(@Param("id") Integer id, Pageable pageable);

    @Query("SELECT MONTH(e.createdAt), COUNT(e) " +
            "FROM Egresso e " +
            "WHERE YEAR(e.createdAt) = :ano " +
            "GROUP BY MONTH(e.createdAt) " +
            "ORDER BY MONTH(e.createdAt)")
    List<Object[]> contarEgressosPorMes(@Param("ano") int ano);

    Page<Egresso> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Egresso> findByHomologadoStatus(AproveStatus aproveStatus, Pageable pageable);

    Page<Egresso> findAllByUserStatus(UserStatus status, Pageable pageable);

}