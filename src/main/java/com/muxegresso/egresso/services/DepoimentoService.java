package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.repositories.DepoimentoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class DepoimentoService {
    @Autowired
    DepoimentoRepository depoimentoRepository;

    @Transactional
    public Depoimento save(@Valid Depoimento depoimento){
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
    public String update(@Valid Depoimento depoimento){
        Depoimento depoimentoObj = this.findById(depoimento.getId());
        depoimentoRepository.save(depoimentoObj);
        return "Depoimento atualizado com sucesso";
    }

    public Page<Depoimento> buscarPorData(Integer ano, Pageable pageable) {
        return depoimentoRepository.findAllByAno(ano, pageable);
    }

    public  Page<Depoimento> buscarRecentes(Integer dias, Pageable pageable){
        return depoimentoRepository.findDepoimentosRecentes(dias,pageable);
    }
}
