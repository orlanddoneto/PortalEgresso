package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Oportunidade;
import com.muxegresso.egresso.domain.dtos.SendOportunidadeDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OportunidadeService {
    Oportunidade save(@Valid Oportunidade oportunidade);

    Oportunidade findById( Integer id);

    Page<Oportunidade> findAll(Pageable pageable);

    Oportunidade delete(Integer id);

    ApiResponse update(@Valid Oportunidade oportunidade);

    Page<Oportunidade> buscarRecentes(Integer dias, Pageable pageable);

    Page<Oportunidade> buscarPorAno(Integer ano, Pageable pageable);

    ApiResponse homologarOportunidade(Integer id, String token, String status);

    ApiResponse criarOportunidade(SendOportunidadeDto request);

    Page<Oportunidade> listarPendentesPorEgressoId(
            Pageable pageable, Integer egressoId, String titulo);

    Page<SendOportunidadeDto> findAllByEgressoId(Pageable pageable, Integer id);

    Page<Oportunidade> listarTodosPendentes(Pageable pageable, String titulo);
}
