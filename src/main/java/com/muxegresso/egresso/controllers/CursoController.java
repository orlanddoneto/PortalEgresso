package com.muxegresso.egresso.controllers;

import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.services.CursoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("v1/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;
    
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
        cursoService.update(depoimento);
        return ResponseEntity.ok().body(depoimento);
    }
}
