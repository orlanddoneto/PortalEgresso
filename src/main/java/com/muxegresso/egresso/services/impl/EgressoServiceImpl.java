package com.muxegresso.egresso.services.impl;


import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.dtos.RequestEgressoDto;
import com.muxegresso.egresso.domain.enums.UserStatus;
import com.muxegresso.egresso.repositories.CargoRepository;
import com.muxegresso.egresso.repositories.CursoRepository;
import com.muxegresso.egresso.repositories.EgressoRepository;
import com.muxegresso.egresso.services.EgressoService;
import com.muxegresso.egresso.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

    @Service
    public class EgressoServiceImpl implements EgressoService {

        @Autowired
        private EgressoRepository egressoRepository;

        @Autowired
        private CursoRepository cursoRepository;

        private final ModelMapper modelMapper =new ModelMapper();
        @Autowired
        private CargoRepository cargoRepository;

        @Override
        public Page<RequestEgressoDto> getAllEgresso(Pageable pageable) {
            return egressoRepository.findAll(pageable).map(egresso -> modelMapper.map(egresso, RequestEgressoDto.class));
        }

        @Override
        public Optional<Egresso> getEgressoByCpf(String cpf) {
            //return modelMapper.map(egressoRepository.findByCpf(cpf), RequestEgressoDto.class);
            return egressoRepository.findByCpf(cpf);
        }

        @Override
        public ApiResponse updateEgresso(@Valid RequestEgressoDto requestEgressoDto) {
            var egresso = modelMapper.map(requestEgressoDto, Egresso.class);
            egressoRepository.save(egresso);
            return new ApiResponse(true, egresso.toString());
        }

        @Override
        public boolean existsByCpf(@NotBlank String cpf) {return egressoRepository.existsByCpf(cpf);}

        @Override
        public boolean existsByEmail(String email) {
            return egressoRepository.existsByEmail(email);
        }

        @Override
        public Egresso save(RequestEgressoDto egresso) {
            var egressoEntity = modelMapper.map(egresso, Egresso.class);
            egressoEntity.setUserStatus(UserStatus.ACTIVE);
            egressoEntity.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
            egressoEntity.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
            egressoRepository.save(egressoEntity);

            return egressoEntity;
        }

        @Override
        public Optional<Egresso> findById(Integer id) {
            return egressoRepository.findById(id);
        }

        @Override
        public void updateEgresso(Egresso egresso, RequestEgressoDto requestEgressoDto) {
            BeanUtils.copyProperties(egresso, requestEgressoDto);

            egresso.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

            egressoRepository.save(egresso);

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
        public Egresso findByCurso(Curso curso) {
            cursoRepository.findById(curso.getId()).orElseThrow(()-> new ResourceNotFoundException(curso.getId()));

            return egressoRepository.findByCurso(curso).get();
        }

        @Override
        public Egresso findByCargo(Cargo cargo) {
            cargoRepository.findById(cargo.getId());
            return egressoRepository.findByCargo(cargo);
        }

        @Override
        public List<Egresso> findAllEgresso() {
            return egressoRepository.findAll();
        }

        @Override
        public void delete(Integer id) {
            var egresso = egressoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));
            egressoRepository.delete(egresso);
        }

        @Override
        public List<Egresso> findALLByCargo(Cargo cargo) {
            return egressoRepository.findAllByCargo();
        }

        @Override
        public boolean efetuarLogin(String mail, String senha1) {
            return false;
        }

        @Override
        public Egresso update(@NotNull Egresso egresso) {
            return egressoRepository.save(egresso);
        }
    }
