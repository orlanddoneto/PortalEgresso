package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.dtos.RequestEgressoDto;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface EgressoService {

    Page<RequestEgressoDto> getAllEgresso(Pageable pageable);

    Optional<Egresso> getEgressoByCpf(@NotBlank String cpf);

    ApiResponse updateEgresso(@Valid RequestEgressoDto requestEgressoDto);

    boolean existsByCpf(@NotBlank String cpf);

    boolean existsByEmail(@NotBlank String email);

    ApiResponse save(@Valid RequestEgressoDto egresso);

    Optional<Egresso> findById(Integer id);

    void updateEgresso(Egresso egresso, RequestEgressoDto requestEgressoDto);

    Page<Egresso> findAll(Specification<Egresso> spec, Pageable pageable);

    boolean existsById(Integer id);
}