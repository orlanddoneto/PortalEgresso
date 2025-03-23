package com.muxegresso.egresso.controllers;

import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.dtos.RequestEgressoDto;
import com.muxegresso.egresso.services.CursoService;
import com.muxegresso.egresso.services.Curso_EgressoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("v1/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private Curso_EgressoService cursoEgressoService;

    private ModelMapper modelMappper = new ModelMapper();

    @GetMapping
    public ResponseEntity<Page<Curso>> findAll(Pageable pageable){
        Page<Curso> list = cursoService.findAll(pageable);

        return ResponseEntity.ok().body(list);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Curso> findById(@PathVariable Integer id){
        Curso user = cursoService.findById(id);
        return ResponseEntity.ok().body(user);
    }
    @GetMapping("/by-name/{name}")
    public ResponseEntity<Page<Curso>> getCursosByName(@PathVariable String name, Pageable pageable){
        var cursos = cursoService.getCursosByName(name, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(cursos);
    }


    @PostMapping
    @Transactional
    public ResponseEntity<Curso> create(@RequestBody @Valid Curso depoimento){
        depoimento = cursoService.save(depoimento);
        URI uri =
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id").buildAndExpand(new Curso()).toUri();

        return ResponseEntity.created(uri).body(depoimento);
    }

    @DeleteMapping(value = "/totalDelete/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Curso> update(@RequestBody Curso depoimento){

        return ResponseEntity.ok().body(cursoService.update(depoimento));
    }

    @GetMapping("/{id}/egressos")
    public ResponseEntity<Page<Egresso>> getEgressosByCurso(@PathVariable Integer id, Pageable pageable) {
        Page<Egresso> egressos = cursoEgressoService.findEgressosByCursoId(id, pageable);
        return ResponseEntity.ok().body(egressos);
    }


}
