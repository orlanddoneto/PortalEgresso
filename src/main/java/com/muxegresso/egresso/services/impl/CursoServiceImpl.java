package com.muxegresso.egresso.services.impl;


import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.repositories.CursoRepository;
import com.muxegresso.egresso.services.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CursoServiceImpl implements CursoService {

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
    public Curso update(@Valid Curso curso){
        Curso cursoObj = this.findById(curso.getId());
        BeanUtils.copyProperties(curso,cursoObj,"id");

        return cursoRepository.save(cursoObj);
    }

    @Override
    public Page<Curso> getCursosByName(String name, Pageable pageable) {
        return cursoRepository.findByNomeContainingIgnoreCase(name, pageable);
    }
}
