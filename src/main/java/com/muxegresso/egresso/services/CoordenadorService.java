package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CoordenadorService {
    
    boolean efetuarLogin(String email, String senha);

    Page<Coordenador> getCoordenadoresByName(String name, Pageable pageable);

    Coordenador save(@Valid Coordenador coordenador);

    Page<Coordenador> findAll(Pageable pageable);

    Coordenador delete(Integer id);

    Coordenador update(@Valid Coordenador coordenador);

    void RegistrarEgresso(Egresso egresso);

    Curso registrarCurso(@Valid Curso curso, @Valid Coordenador coordenador);

    void AdicionarCargo(@Valid Egresso egresso, @Valid Cargo cargo);

    void AdicionarDepoimento(@Valid Egresso egresso, @Valid Depoimento depoimento);

    Coordenador findById(Integer id);

    Depoimento lincarDepoimentoEgresso(@NotNull Egresso egresso, @NotNull Depoimento depoimento);

    Curso addCurso(@NotNull String email, @NotBlank String nome, @NotBlank String nivel);

    Cargo addCargo(@NotNull Egresso egresso, @NotNull Cargo cargo);

    public boolean existsByEmail(String email);
}
