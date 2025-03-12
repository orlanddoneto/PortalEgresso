package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.domain.Oportunidade;
import com.muxegresso.egresso.domain.dtos.UsuarioDTO;
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

    public ApiResponse homologarOportunidade(Integer id, UsuarioDTO usuarioDTO);

}
