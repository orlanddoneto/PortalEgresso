package com.muxegresso.egresso.services.impl;


import com.muxegresso.egresso.domain.*;
import com.muxegresso.egresso.domain.dtos.RequestEgressoDto;
import com.muxegresso.egresso.domain.enums.AproveStatus;
import com.muxegresso.egresso.domain.enums.UserStatus;
import com.muxegresso.egresso.infra.security.TokenService;
import com.muxegresso.egresso.repositories.CursoRepository;
import com.muxegresso.egresso.repositories.EgressoRepository;
import com.muxegresso.egresso.services.Curso_EgressoService;
import com.muxegresso.egresso.services.EgressoService;
import com.muxegresso.egresso.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.apache.catalina.User;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

    @Service
    public class EgressoServiceImpl implements EgressoService {

    @Autowired
    private EgressoRepository egressoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Curso_EgressoService cursoEgressoService;

    @Autowired
    private TokenService tokenService;

    @Override
    public Page<Egresso> findAllEgresso(Pageable pageable) {
        return egressoRepository.findAll(pageable);
    }

    @Override
    public Page<Egresso> findAllEgressoByUserStatus(Pageable pageable) {
        return egressoRepository.findAllByUserStatus(UserStatus.ACTIVE, pageable);
    }

    @Override
    public Optional<Egresso> getEgressoByCpf(String cpf) {
        //return modelMapper.map(egressoRepository.findByCpf(cpf), RequestEgressoDto.class);
        return egressoRepository.findByCpf(cpf);
    }

    public Page<Egresso> getEgressosByName(String nome, Pageable pageable) {
        return egressoRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    @Override
    public boolean existsByCpf(@NotBlank String cpf) {return egressoRepository.existsByCpf(cpf);}

    @Override
    public boolean existsByEmail(String email) {
        return egressoRepository.existsByEmail(email);
    }

    @Override
    public Egresso save(RequestEgressoDto egresso) {

        Curso curso = cursoRepository.findById(egresso.getIdCurso()).orElseThrow(() -> new ResourceNotFoundException(egresso.getIdCurso()));

        egresso.setHomologado(AproveStatus.PENDING);
        var egressoEntity = modelMapper.map(egresso, Egresso.class);
        egressoEntity.setUserStatus(UserStatus.ACTIVE);
        egressoEntity.setHomologadoStatus(AproveStatus.PENDING);
        egressoEntity.setSenha(passwordEncoder.encode(egressoEntity.getSenha()));
        egressoEntity.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        egressoEntity.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        var egressoSaved = egressoRepository.save(egressoEntity);

        Curso_Egresso cursoEgresso = new Curso_Egresso();

        cursoEgresso.setCurso(curso);
        cursoEgresso.setEgresso(egressoSaved);
        cursoEgresso.setAno_inicio(egresso.getAno_inicio());
        cursoEgresso.setAno_fim(egresso.getAno_fim());
        cursoEgressoService.save(cursoEgresso);

        return egressoSaved;

    }

    @Override
    public Optional<Egresso> findById(Integer id) {
        return egressoRepository.findById(id);
    }

    @Override
    public void updateEgresso(RequestEgressoDto requestEgressoDto) {

        Optional<Egresso> existente = egressoRepository.findById(requestEgressoDto.getId());
        requestEgressoDto.setHomologado(existente.get().getHomologadoStatus());
        if (requestEgressoDto.getSenha() != null && !requestEgressoDto.getSenha().isEmpty()) {
            requestEgressoDto.setSenha(passwordEncoder.encode(requestEgressoDto.getSenha()));
        }
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(requestEgressoDto, existente.get());

        if (requestEgressoDto.getIdCurso() != null) {
            Curso newCurso = cursoRepository.findById(requestEgressoDto.getIdCurso()).orElseThrow(()-> new ResourceNotFoundException(requestEgressoDto.getIdCurso()));
            Curso_Egresso cursoEgresso = cursoEgressoService.findById(requestEgressoDto.getIdCurso());
            cursoEgresso.setCurso(newCurso);
            cursoEgressoService.save(cursoEgresso);
        }

        existente.get().setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        egressoRepository.save(existente.get());
    }


    @Override
    public Page<Egresso> findAll(Specification<Egresso> spec, Pageable pageable) {
        return egressoRepository.findAll(spec, pageable);
    }

    @Override
    public boolean existsById(Integer id) {
        return egressoRepository.existsById(id);
    }

    @Override
    public void delete(Integer id) {
        var egresso = egressoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));
        egressoRepository.delete(egresso);
    }

    @Override
    public ApiResponse homologarEgresso(Integer id, String token, String status) {
        String role = tokenService.getRoleFromToken(token);

        if (!"COORDENADOR_ROLE".equals(role)) {
            return new ApiResponse(false, "Erro: apenas coordenadores podem homologar egressos.");
        }

        AproveStatus novoStatus;
        try {
            novoStatus = AproveStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ApiResponse(false, "Status inválido: " + status);
        }

        Egresso egresso = egressoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (egresso.getHomologadoStatus() == novoStatus) {
            return new ApiResponse(false, "Egresso já está com status: " + novoStatus);
        }

        egresso.setHomologadoStatus(novoStatus);
        egressoRepository.save(egresso);

        return new ApiResponse(true, "Status do egresso atualizado para: " + novoStatus);
    }

    @Override
    public Page<Egresso> listarEgressosPendentes(Pageable pageable) {
        return egressoRepository.findByHomologadoStatus(AproveStatus.PENDING, pageable);
    }

    @Override
    public Optional<Egresso> getEgressoById(Integer id) {
        return egressoRepository.findById(id);
    }

    @Override
    public void logicalDelete(Integer id) {
        var egresso = egressoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));
        egresso.setUserStatus(UserStatus.BLOCKED);
    }

        @Override
    public Page<Cargo> findAllCargos(Integer idEgresso, Pageable pageable) {
         return egressoRepository.findCargosById(idEgresso,pageable);
    }

}

