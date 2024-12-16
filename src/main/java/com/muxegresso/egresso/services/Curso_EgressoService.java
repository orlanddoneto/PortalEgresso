package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.Curso_Egresso;
import com.muxegresso.egresso.repositories.Curso_EgressoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Curso_EgressoService {

    @Autowired
    Curso_EgressoRepository cursoEgressoRepository;

    @Transactional
    public Curso_Egresso save(@Valid Curso_Egresso cursoEgresso){
        return cursoEgressoRepository.save(cursoEgresso);
    }

    public Curso_Egresso findById( Integer id){
        Curso_Egresso cursoEgresso = cursoEgressoRepository.findById(id).orElseThrow(()->new RuntimeException("ID não presente no sistema."));
        return cursoEgresso;
    }

    public Page<Curso_Egresso> findAll(Pageable pageable){
        return cursoEgressoRepository.findAll(pageable);
    }

    @Transactional
    public Curso_Egresso delete(Integer id){
        Curso_Egresso cursoEgresso = this.findById(id);
        cursoEgressoRepository.delete(cursoEgresso);
        return cursoEgresso;
    }

    @Transactional
    public String update(@Valid Curso_Egresso cursoEgresso){
        Curso_Egresso cursoEgressoObj = this.findById(cursoEgresso.getId());
        cursoEgressoRepository.save(cursoEgressoObj);
        return "Curso_Egresso atualizado com sucesso";

    }
}
