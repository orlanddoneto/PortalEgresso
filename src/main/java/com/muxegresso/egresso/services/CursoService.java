package com.muxegresso.egresso.services;

import aj.org.objectweb.asm.commons.Remapper;
import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.dtos.RequestCursoDto;
import com.muxegresso.egresso.repositories.CursoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface CursoService{

    Curso save(@Valid Curso egresso);

    Curso findById( Integer id);

    Page<RequestCursoDto> findAll(Pageable pageable);

    Curso delete(Integer id);

    Curso update(@Valid Curso curso);

    Page<Curso> getCursosByName(String name, Pageable pageable);
}
