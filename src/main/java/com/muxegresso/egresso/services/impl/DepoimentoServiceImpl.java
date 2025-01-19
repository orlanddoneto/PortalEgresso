package com.muxegresso.egresso.services.impl;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.repositories.DepoimentoRepository;
import com.muxegresso.egresso.services.DepoimentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;


@Service
public class DepoimentoServiceImpl implements DepoimentoService {
    @Autowired
    DepoimentoRepository depoimentoRepository;

    @Transactional
    public Depoimento save(@Valid Depoimento depoimento){
        depoimento.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        depoimento.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        return depoimentoRepository.save(depoimento);
    }

    public Depoimento findById( Integer id){
        Depoimento depoimento = depoimentoRepository.findById(id).orElseThrow(()->new RuntimeException("ID não presente no sistema."));
        return depoimento;
    }

    public Page<Depoimento> findAll(Pageable pageable){
        return depoimentoRepository.findAll(pageable);
    }

    @Transactional
    public Depoimento delete(Integer id){
        Depoimento depoimento = this.findById(id);
        depoimentoRepository.delete(depoimento);
        return depoimento;
    }

    @Transactional
    public ApiResponse update(@Valid Depoimento depoimento){
        depoimentoRepository.save(depoimento);
        return new ApiResponse(true,"Depoimento atualizado com sucesso");
    }

    public Page<Depoimento> buscarPorData(Integer ano, Pageable pageable) {
        return depoimentoRepository.findAllByAno(ano, pageable);
    }

    public  Page<Depoimento> buscarRecentes(Integer dias, Pageable pageable){
        return depoimentoRepository.findDepoimentosRecentes(dias,pageable);
    }
}
