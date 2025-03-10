package com.muxegresso.egresso.controllers;

import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.services.CargoService;
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
@RequestMapping("v1/cargo")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @GetMapping
    public ResponseEntity<Page<Cargo>> findAll(Pageable pageable) {
        Page<Cargo> list = cargoService.findAll(pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cargo> findById(@PathVariable Integer id) {
        Cargo cargo = cargoService.findById(id);
        return ResponseEntity.ok().body(cargo);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Cargo> create(@RequestBody @Valid Cargo cargo) {
        Cargo savedCargo = cargoService.save(cargo);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCargo.getId()).toUri();
        return ResponseEntity.created(uri).body(savedCargo);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Cargo> update(@RequestBody @Valid Cargo cargo) {
        cargoService.update(cargo);
        Cargo updatedCargo = cargoService.findById(cargo.getId());
        return ResponseEntity.ok().body(updatedCargo);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        cargoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
