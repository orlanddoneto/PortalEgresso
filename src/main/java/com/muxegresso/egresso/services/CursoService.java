package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.repositories.CursoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CursoService {
    @Autowired
    CursoRepository cursoRepository;

    @Transactional
    public Curso save(@Valid Curso egresso){
        return cursoRepository.save(egresso);
    }

    public Curso findById( Integer id){
        Curso curso = cursoRepository.findById(id).orElseThrow(()->new RuntimeException("ID não presente no sistema."));
        return curso;
    }

    public Page<Curso> findAll(Pageable pageable){
        return cursoRepository.findAll(pageable);
    }

    @Transactional
    public Curso delete(Integer id){
        Curso curso = this.findById(id);
        cursoRepository.delete(curso);
        return curso;
    }

    @Transactional
    public String update(@Valid Curso curso){
        Curso cursoObj = this.findById(curso.getId());
        cursoRepository.save(cursoObj);
        return "Curso atualizado com sucesso";

    }
}
