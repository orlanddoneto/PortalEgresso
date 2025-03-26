package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.dtos.RequestEgressoDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface EgressoService {

    Page<Egresso> findAllEgresso(Pageable pageable);

    Optional<Egresso> getEgressoByCpf(@NotBlank String cpf);

    Page<Egresso> getEgressosByName(String nome, Pageable pageable);

    boolean existsByCpf(@NotBlank String cpf);

    boolean existsByEmail(@NotBlank String email);

    Egresso save(@Valid RequestEgressoDto egresso);

    Optional<Egresso> findById(Integer id);

    void updateEgresso(RequestEgressoDto requestEgressoDto);

    Page<Egresso> findAll(Specification<Egresso> spec, Pageable pageable);

    boolean existsById(Integer id);

    void delete(Integer id);

    Page<Cargo> findAllCargos(Integer idEgresso, Pageable pageable);


    ApiResponse homologarEgresso(Integer id, String token, String status);

    Page<Egresso> listarEgressosPendentes(Pageable pageable);

    Optional<Egresso> getEgressoById(Integer id);

    void logicalDelete(Integer id);

    Page<Egresso> findAllEgressoByUserStatus(Pageable pageable);
}