package com.muxegresso.egresso.controllers;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.domain.Oportunidade;
import com.muxegresso.egresso.domain.dtos.UsuarioDTO;
import com.muxegresso.egresso.services.OportunidadeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("v1/oportunidade")
public class OportunidadeController {

    @Autowired
    OportunidadeService oportunidadeService;

    @GetMapping
    public ResponseEntity<Page<Oportunidade>> findAll(Pageable pageable){
        Page<Oportunidade> list = oportunidadeService.findAll(pageable);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Oportunidade> findById(@PathVariable Integer id){
        Oportunidade user = oportunidadeService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Object> create(@RequestBody @Valid Oportunidade oportunidade){
        oportunidadeService.save(oportunidade);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true,"Oportunidade criada!"));
    }

    @DeleteMapping(value = "/totalDelete/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        oportunidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Object> update(@RequestBody Oportunidade oportunidade){
        ApiResponse response = oportunidadeService.update(oportunidade);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/{ano}")
    public ResponseEntity<Page<Oportunidade>> buscarPorAno (@PathVariable Integer ano, Pageable pageable){
        Page<Oportunidade> oportunidades = oportunidadeService.buscarPorAno(ano,pageable);
        return ResponseEntity.ok().body(oportunidades);
    }

    @GetMapping(value = "/{dias}")
    public ResponseEntity<Page<Oportunidade>> buscarRecentes (@PathVariable Integer dias, Pageable pageable){
        Page<Oportunidade> oportunidades = oportunidadeService.buscarRecentes(dias,pageable);
        return ResponseEntity.ok().body(oportunidades);
    }

    @PutMapping("/{id}/homologar")
    public ResponseEntity<Object> homologar(@PathVariable Integer id, @RequestParam UsuarioDTO usuarioDTO) {
        ApiResponse response = oportunidadeService.homologarOportunidade(id,usuarioDTO);
        return ResponseEntity.ok().body(response);
    }


}
