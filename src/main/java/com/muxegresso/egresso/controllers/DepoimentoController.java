package com.muxegresso.egresso.controllers;

import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.services.DepoimentoService;
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
@RequestMapping("v1/depoimento")
public class DepoimentoController {

    @Autowired
    private DepoimentoService depoimentoService;

    @GetMapping
    public ResponseEntity<Page<Depoimento>> findAll(Pageable pageable){
        Page<Depoimento> list = depoimentoService.findAll(pageable);

        return ResponseEntity.ok().body(list);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Depoimento> findById(@PathVariable Integer id){
        Depoimento user = depoimentoService.findById(id);
        return ResponseEntity.ok().body(user);
    }
    @PostMapping
    @Transactional
    public ResponseEntity<Depoimento> create(@RequestBody @Valid Depoimento depoimento){
        depoimento = depoimentoService.save(depoimento);
        URI uri =
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id").buildAndExpand(new Depoimento()).toUri();

        return ResponseEntity.created(uri).body(depoimento);
    }

    @DeleteMapping(value = "/totalDelete/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        depoimentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Depoimento> update(@RequestBody Depoimento depoimento){
        depoimentoService.update(depoimento);
        return ResponseEntity.ok().body(depoimento);
    }
}
