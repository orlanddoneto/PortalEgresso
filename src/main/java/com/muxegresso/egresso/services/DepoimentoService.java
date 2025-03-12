package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.domain.dtos.UsuarioDTO;
import com.muxegresso.egresso.domain.enums.StatusDepoimento;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepoimentoService {
    Depoimento save(@Valid Depoimento depoimento);

    Depoimento findById( Integer id);

    Page<Depoimento> findAll(Pageable pageable);

    Depoimento delete(Integer id);

    ApiResponse update(@Valid Depoimento depoimento);

    Page<Depoimento> buscarRecentes(Integer dias, Pageable pageable);

    Page<Depoimento> buscarPorData(Integer ano, Pageable pageable);

    ApiResponse alterarStatus(Integer id, StatusDepoimento novoStatus);

    public ApiResponse homologarDepoimento(Integer id, UsuarioDTO usuarioDTO);
}
