package com.muxegresso.egresso.services.impl;


import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.dtos.RequestCursoDto;
import com.muxegresso.egresso.repositories.CursoRepository;
import com.muxegresso.egresso.services.CursoService;
import com.muxegresso.egresso.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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

    ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public Curso save(@Valid Curso egresso){
        return cursoRepository.save(egresso);
    }

    public Curso findById( Integer id){
        Curso curso = cursoRepository.findById(id).orElseThrow(()->new RuntimeException("ID não presente no sistema."));
        return curso;
    }

    public Page<RequestCursoDto> findAll(Pageable pageable){
        return cursoRepository.findAll(pageable).map(curso -> modelMapper.map(curso, RequestCursoDto.class));
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
