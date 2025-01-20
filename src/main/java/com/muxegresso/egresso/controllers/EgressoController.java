package com.muxegresso.egresso.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.dtos.RequestEgressoDto;
import com.muxegresso.egresso.services.impl.EgressoServiceImpl;
import com.muxegresso.egresso.specifications.SpecificationTemplate;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("v1/egresso")
public class EgressoController {

    @Autowired
    private EgressoServiceImpl egressoServiceImpl;

    private ModelMapper modelMappper = new ModelMapper();

    @GetMapping
    public ResponseEntity<Page<Egresso>> getAllEgresso(
            SpecificationTemplate.EgressoSpec spec,
            @PageableDefault(size = 10, sort = "id") Pageable pageable){

        var egressolist = egressoServiceImpl.findAll(spec, pageable);

        if (!egressolist.isEmpty()) {
            for (Egresso egresso: egressolist.toList()){
                egresso.add(linkTo(methodOn(EgressoController.class).getEgressoByCpf(egresso.getCpf())).withSelfRel());
            }
        }
        //return ResponseEntity.ok().body(egressolist.map(egresso -> modelMappper.map(egresso, RequestEgressoDto.class)));
        return ResponseEntity.ok().body(egressolist);
    }

    @GetMapping("{cpf}")
    public ResponseEntity<Object> getEgressoByCpf(@PathVariable(value = "cpf") String cpf){
        var egresso = egressoServiceImpl.getEgressoByCpf(cpf);

        if (!egresso.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cpf not found. ");
        }
        return ResponseEntity.status(HttpStatus.OK).body(egresso.get());
    }


    @PostMapping
    public ResponseEntity<Object> registrerEgresso(@RequestBody
                                                   @JsonView(RequestEgressoDto.EgressoView.RegistrationPost.class) RequestEgressoDto requestEgressoDto){

        if (!egressoServiceImpl.existsByCpf(requestEgressoDto.getCpf())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "CPF já cadastrado !!!"));
        }
        if (!egressoServiceImpl.existsByEmail(requestEgressoDto.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "Email fornecido já cadastrado !!!"));
        }

        var response = egressoServiceImpl.save(requestEgressoDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    @PutMapping
    public ResponseEntity<Object> updateEgresso(@RequestBody @JsonView(RequestEgressoDto.EgressoView.EgressoUpdate.class) RequestEgressoDto requestEgressoDto){

        var egressoOptional = egressoServiceImpl.findById(requestEgressoDto.getId());
        if (egressoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Egresso não encontrado"));
        }
        egressoServiceImpl.updateEgresso(egressoOptional.get(), requestEgressoDto);

        return ResponseEntity.ok().body(new ApiResponse(true, "id: "+requestEgressoDto.getId()+"- Egresso Atualizado !"));
    }

    @Transactional
    @PatchMapping("/updatepass")
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody @JsonView(RequestEgressoDto.EgressoView.PasswordUpdate.class) RequestEgressoDto requestEgressoDto){

        if (!egressoServiceImpl.existsById(requestEgressoDto.getId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "ID não encontrado"));
        }

        return ResponseEntity.ok().body(new ApiResponse(true, "Senha atualizada"));

    }
}