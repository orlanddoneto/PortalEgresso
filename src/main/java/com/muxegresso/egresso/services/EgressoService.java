package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.dtos.RequestEgressoDto;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface EgressoService {

    List<Egresso> findAllEgresso();

    Optional<Egresso> getEgressoByCpf(@NotBlank String cpf);

    ApiResponse updateEgresso(@Valid RequestEgressoDto requestEgressoDto);

    boolean existsByCpf(@NotBlank String cpf);

    boolean existsByEmail(@NotBlank String email);

    Egresso save(@Valid RequestEgressoDto egresso);

    Optional<Egresso> findById(Integer id);

    void updateEgresso(Egresso egresso, RequestEgressoDto requestEgressoDto);

    Page<Egresso> findAll(Specification<Egresso> spec, Pageable pageable);

    boolean existsById(Integer id);

    void delete(Integer id);

    /*
    List<Egresso> findAllByCurso(Integer idCurso);

    List<Egresso> findAllByCargo(Integer idCargo);
    */
    public String efetuarLogin(String email, String senha);
}