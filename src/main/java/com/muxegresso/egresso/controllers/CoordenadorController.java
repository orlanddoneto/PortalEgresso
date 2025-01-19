package com.muxegresso.egresso.controllers;

import com.muxegresso.egresso.domain.Coordenador;
import com.muxegresso.egresso.services.CoordenadorService;
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
@RequestMapping("v1/coordenador")
public class CoordenadorController {

    @Autowired
    private CoordenadorService coordenadorService;

    @GetMapping
    public ResponseEntity<Page<Coordenador>> findAll(Pageable pageable){
        Page<Coordenador> list = coordenadorService.findAll(pageable);

        return ResponseEntity.ok().body(list);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Coordenador> findById(@PathVariable Integer id){
        Coordenador coordenador = coordenadorService.findById(id);
        return ResponseEntity.ok().body(coordenador);
    }
    @PostMapping
    @Transactional
    public ResponseEntity<Coordenador> create(@RequestBody @Valid Coordenador coordenador){
        coordenador = coordenadorService.save(coordenador);
        URI uri =
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id").buildAndExpand(new Coordenador()).toUri();

        return ResponseEntity.created(uri).body(coordenador);
    }

    @DeleteMapping(value = "/totalDelete/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        coordenadorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Coordenador> update(@RequestBody Coordenador coordenador){
        coordenadorService.update(coordenador);
        return ResponseEntity.ok().body(coordenador);
    }
}

