package com.muxegresso.egresso.controllers;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.domain.dtos.RequestDepoimentoDto;
import com.muxegresso.egresso.domain.enums.StatusDepoimento;
import com.muxegresso.egresso.services.DepoimentoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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

    private ModelMapper modelMappper = new ModelMapper();

    @GetMapping
    public ResponseEntity<Page<Depoimento>> findAll(Pageable pageable){
        Page<Depoimento> list = depoimentoService.findAll(pageable);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/pendentes-por-egresso")
    public Page<Depoimento> listarPendentes(Pageable pageable, @RequestParam Integer idEgresso, @RequestParam(required = false) String texto) {
        return depoimentoService.listarPendentesPorEgressoId(pageable, idEgresso, texto);
    }

    @GetMapping("/pendentes")
    public Page<Depoimento> listarTodosPendentes(Pageable pageable, @RequestParam(required = false) String texto) {
        return depoimentoService.listarTodosPendentes(pageable, texto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Depoimento> findById(@PathVariable Integer id){
        Depoimento user = depoimentoService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/by-egresso/{id}")
    public ResponseEntity<Page<RequestDepoimentoDto>> findAllByEgressoId(Pageable pageable, @PathVariable Integer id){
        var depoimentos = depoimentoService.findAllByEgressoId(pageable, id);
        return ResponseEntity.ok().body(depoimentos);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Depoimento> create(@RequestBody @Valid RequestDepoimentoDto depoimento){
        var depoimentoEntity = depoimentoService.save(depoimento);
        URI uri =
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id").buildAndExpand(new Depoimento()).toUri();

        return ResponseEntity.created(uri).body(depoimentoEntity);
    }

    @PostMapping("/enviar-depoimento")
    public ResponseEntity<ApiResponse> enviarDepoimento(@RequestBody RequestDepoimentoDto request) {
        var response = depoimentoService.enviarDepoimento(request);

        return ResponseEntity.ok(response);
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
        ApiResponse response = depoimentoService.update(depoimento);
        return ResponseEntity.ok().body(depoimento);
    }

    @GetMapping(value = "/{ano}")
    public ResponseEntity<Page<Depoimento>> buscarPorAno (@PathVariable Integer ano, Pageable pageable){
        Page<Depoimento> depoimentos = depoimentoService.buscarPorData(ano,pageable);
        return ResponseEntity.ok().body(depoimentos);
    }

    @GetMapping(value = "/{dias}")
    public ResponseEntity<Page<Depoimento>> buscarRecentes (@PathVariable Integer dias, Pageable pageable){
        Page<Depoimento> depoimentos = depoimentoService.buscarRecentes(dias,pageable);
        return ResponseEntity.ok().body(depoimentos);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Object> atualizarStatus(
            @PathVariable Integer id,
            @RequestParam StatusDepoimento status) {

        return ResponseEntity.ok().body(depoimentoService.alterarStatus(id, status));
    }

    @PutMapping("/{id}/homologar")
    public ResponseEntity<Object> homologar(@PathVariable Integer id, @RequestParam String token, @RequestParam String status) {
        ApiResponse response = depoimentoService.homologarDepoimento(id, token, status);
        return ResponseEntity.ok().body(response);
    }
}
