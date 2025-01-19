package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CoordenadorService {
    
    String efetuarLogin(String email, String senha);

    Coordenador save(@Valid Coordenador coordenador);

   Page<Coordenador> findAll(Pageable pageable);

    Coordenador delete(Integer id);

    String update(@Valid Coordenador coordenador);

    void RegistrarEgresso(Egresso egresso);

    Curso registrarCurso(@Valid Curso curso, @Valid Coordenador coordenador);

    void AdicionarCargo(@Valid Egresso egresso, @Valid Cargo cargo);

    void AdicionarDepoimento(@Valid Egresso egresso, @Valid Depoimento depoimento);

    Coordenador findById(Integer id);
}
