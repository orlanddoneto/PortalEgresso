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

    Page<RequestEgressoDto> getAllEgresso(Pageable pageable);

    Optional<Egresso> getEgressoByCpf(@NotBlank String cpf);

    ApiResponse updateEgresso(@Valid RequestEgressoDto requestEgressoDto);

    boolean existsByCpf(@NotBlank String cpf);

    boolean existsByEmail(@NotBlank String email);

    Egresso save(@Valid RequestEgressoDto egresso);

    Optional<Egresso> findById(Integer id);

    void updateEgresso(Egresso egresso, RequestEgressoDto requestEgressoDto);

    Page<Egresso> findAll(Specification<Egresso> spec, Pageable pageable);

    boolean existsById(Integer id);

<<<<<<< HEAD
    Egresso findByCurso(Curso curso);

    Egresso findByCargo(Cargo cargo);

    List<Egresso> findAllEgresso();

    void delete(Integer id);

    List<Egresso> findALLByCargo(Cargo cargo);

    boolean efetuarLogin(String mail, String senha1) throws Exception;

    Egresso update(Egresso egresso);
=======
    public String efetuarLogin(String email, String senha);
>>>>>>> 3af1fcb159c93579c7f5335730431f1bb084e35f
}