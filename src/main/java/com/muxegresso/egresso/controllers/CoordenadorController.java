package com.muxegresso.egresso.controllers;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Coordenador;
import com.muxegresso.egresso.domain.dtos.RequestCoordenadorDto;
import com.muxegresso.egresso.domain.dtos.RequestEgressoDto;
import com.muxegresso.egresso.services.CoordenadorService;
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
@RequestMapping("v1/coordenador")
public class CoordenadorController {

    @Autowired
    private CoordenadorService coordenadorService;

    private ModelMapper modelMappper = new ModelMapper();

    @GetMapping
    public ResponseEntity<Page<RequestCoordenadorDto>> findAll(Pageable pageable){
        Page<RequestCoordenadorDto> coordenadorDtos = coordenadorService.findAll(pageable).map(cor -> modelMappper.map(cor, RequestCoordenadorDto.class));

        return ResponseEntity.ok().body(coordenadorDtos);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<RequestCoordenadorDto> findById(@PathVariable Integer id){
        Coordenador coordenador = coordenadorService.findById(id);
        return ResponseEntity.ok().body(modelMappper.map(coordenador, RequestCoordenadorDto.class));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<Page<RequestCoordenadorDto>> getEgressoByName(@PathVariable String name, Pageable pageable){
        var egressos = coordenadorService.getCoordenadoresByName(name, pageable).map(coordenador -> modelMappper.map(coordenador, RequestCoordenadorDto.class));

        return ResponseEntity.status(HttpStatus.OK).body(egressos);
    }
    @PostMapping
    @Transactional
    public ResponseEntity<Object> create(@RequestBody @Valid Coordenador coordenador){
        if (coordenadorService.existsByEmail(coordenador.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "Email já cadastrado !"));
        }

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
        Coordenador atualizado = coordenadorService.update(coordenador);
        return ResponseEntity.ok().body(atualizado);
    }
}

