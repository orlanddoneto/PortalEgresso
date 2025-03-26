package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.domain.dtos.RequestDepoimentoDto;
import com.muxegresso.egresso.domain.enums.StatusDepoimento;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepoimentoService {
    Depoimento save(@Valid RequestDepoimentoDto depoimento);

    Depoimento findById( Integer id);

    Page<Depoimento> findAll(Pageable pageable);

    Depoimento delete(Integer id);

    ApiResponse update(@Valid Depoimento depoimento);

    Page<Depoimento> buscarRecentes(Integer dias, Pageable pageable);

    Page<Depoimento> buscarPorData(Integer ano, Pageable pageable);

    ApiResponse alterarStatus(Integer id, StatusDepoimento novoStatus);

    ApiResponse homologarDepoimento(Integer id, String token, String status);

    ApiResponse enviarDepoimento(RequestDepoimentoDto request);

    Page<RequestDepoimentoDto> findAllByEgressoId(Pageable pageable, Integer id);

    Page<Depoimento> listarPendentesPorEgressoId(Pageable pageable, Integer idEgresso, String texto);

    Page<Depoimento> listarTodosPendentes(Pageable pageable, String texto);
}
