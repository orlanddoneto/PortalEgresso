package com.muxegresso.egresso.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.dtos.RequestEgressoDto;
import com.muxegresso.egresso.services.Curso_EgressoService;
import com.muxegresso.egresso.services.EgressoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/egresso")
public class EgressoController {

    @Autowired
    private EgressoService egressoServiceImpl;

    private ModelMapper modelMappper = new ModelMapper();

    @Autowired
    private Curso_EgressoService cursoEgressoService;
    @Autowired
    private EgressoService egressoService;

    @GetMapping
    public ResponseEntity<Page<Egresso>> getAllEgresso(Pageable pageable){

        Page<Egresso> egressolist = egressoServiceImpl.findAllEgressoByUserStatus(pageable);

        //return ResponseEntity.ok().body(egressolist.map(egresso -> modelMappper.map(egresso, RequestEgressoDto.class)));
        return ResponseEntity.ok().body(egressolist);
    }

    @GetMapping("/pendentes")
    public Page<Egresso> listarPendentes(Pageable pageable) {
        return egressoService.listarEgressosPendentes(pageable);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Object> getEgressoByCpf(@PathVariable(value = "cpf") String cpf){
        var egresso = egressoServiceImpl.getEgressoByCpf(cpf);

        if (!egresso.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cpf not found. ");
        }
        return ResponseEntity.status(HttpStatus.OK).body(egresso.get());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getEgressoById(@PathVariable Integer id){
        var egresso = egressoServiceImpl.getEgressoById(id);

        if (!egresso.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id not found. ");
        }
        return ResponseEntity.status(HttpStatus.OK).body(egresso.get());
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<Page<RequestEgressoDto>> getEgressoByName(@PathVariable String name, Pageable pageable){
        var egressos = egressoServiceImpl.getEgressosByName(name, pageable).map(egresso -> modelMappper.map(egresso, RequestEgressoDto.class));

        return ResponseEntity.status(HttpStatus.OK).body(egressos);
    }


    @PostMapping
    public ResponseEntity<Object> registrerEgresso(@RequestBody
                                                   @JsonView(RequestEgressoDto.EgressoView.RegistrationPost.class) RequestEgressoDto requestEgressoDto){

        if (egressoServiceImpl.existsByCpf(requestEgressoDto.getCpf())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "CPF já cadastrado !"));
        }
        if (egressoServiceImpl.existsByEmail(requestEgressoDto.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "Email fornecido já cadastrado !"));
        }

        var response = egressoServiceImpl.save(requestEgressoDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    @PutMapping
    public ResponseEntity<Object> updateEgresso(@RequestBody @JsonView(RequestEgressoDto.EgressoView.EgressoUpdate.class) RequestEgressoDto requestEgressoDto){

        var egressoOptional = egressoServiceImpl.findById(requestEgressoDto.getId());
        if (!egressoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Egresso não encontrado"));
        }
        egressoServiceImpl.updateEgresso(requestEgressoDto);
        return ResponseEntity.ok().body(new ApiResponse(true, "id: " + requestEgressoDto.getId() + "- Egresso Atualizado !"));

    }

    @Transactional
    @PatchMapping("/updatepass")
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody @JsonView(RequestEgressoDto.EgressoView.PasswordUpdate.class) RequestEgressoDto requestEgressoDto){

        if (!egressoServiceImpl.existsById(requestEgressoDto.getId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "ID não encontrado"));
        }

        return ResponseEntity.ok().body(new ApiResponse(true, "Senha atualizada"));

    }

    @PutMapping("/{id}/homologar")
    public ResponseEntity<Object> homologar(@PathVariable Integer id, @RequestParam String token, @RequestParam String status) {
        ApiResponse response = egressoServiceImpl.homologarEgresso(id, token, status);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}/cursos")
    public ResponseEntity<Page<Curso>> getCursosByEgresso(@PathVariable("id") Integer idEgresso, Pageable pageable) {
        Page<Curso> cursos = cursoEgressoService.findCursosByEgressoId(idEgresso,pageable);
        return ResponseEntity.ok().body(cursos);
    }
    @DeleteMapping(value = "/totalDelete/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        egressoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/logicalDelete/{id}")
    @Transactional
    public ResponseEntity<Void> logicalDelete(@PathVariable Integer id){
        egressoService.logicalDelete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/cargos")
    public ResponseEntity<Page<Cargo>> getCargosByEgressoId(@PathVariable("id") Integer id, Pageable pageable) {
        Page<Cargo> cargos = egressoServiceImpl.findAllCargos(id, pageable);
        return ResponseEntity.ok().body(cargos);
    }


}