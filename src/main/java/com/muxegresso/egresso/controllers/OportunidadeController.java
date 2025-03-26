package com.muxegresso.egresso.controllers;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Oportunidade;
import com.muxegresso.egresso.domain.dtos.RequestDepoimentoDto;
import com.muxegresso.egresso.domain.dtos.SendOportunidadeDto;
import com.muxegresso.egresso.services.OportunidadeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/pendentes-por-egresso")
    public Page<Oportunidade> listarPendentesPorEgresso(Pageable pageable,@RequestParam Integer egressoId, @RequestParam(required = false) String titulo) {
        return oportunidadeService.listarPendentesPorEgressoId(pageable, egressoId, titulo);
    }

    @GetMapping("/pendentes")
    public Page<Oportunidade> listarTodosPendentes(Pageable pageable, @RequestParam(required = false) String titulo) {
        return oportunidadeService.listarTodosPendentes(pageable, titulo);
    }

    @GetMapping(value = "/by-egresso/{id}")
    public ResponseEntity<Page<SendOportunidadeDto>> findAllByEgressoId(Pageable pageable, @PathVariable Integer id){
        var depoimentos = oportunidadeService.findAllByEgressoId(pageable, id);
        return ResponseEntity.ok().body(depoimentos);
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

    @PostMapping("/enviar-oportunidade")
    public ResponseEntity<ApiResponse> enviarOportunidade(@RequestBody SendOportunidadeDto request) {
        ApiResponse response = oportunidadeService.criarOportunidade(request);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<Object> homologar(@PathVariable Integer id, @RequestParam String token, @RequestParam String status) {
        ApiResponse response = oportunidadeService.homologarOportunidade(id, token, status);
        return ResponseEntity.ok().body(response);
    }


}
