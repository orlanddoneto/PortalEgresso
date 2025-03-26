package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.repositories.CargoRepository;
import com.muxegresso.egresso.repositories.EgressoRepository;
import com.muxegresso.egresso.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CargoService {
    @Autowired
    CargoRepository cargoRepository;

    @Autowired
    EgressoRepository egressoRepository;

    @Transactional
    public Cargo save(@Valid Cargo cargo){
        return cargoRepository.save(cargo);
    }

    public Cargo findById( Integer id){
        Cargo cargo = cargoRepository.findById(id).orElseThrow(()->new RuntimeException("ID não presente no sistema."));
        return cargo;
    }

    public Page<Cargo> findAll(Pageable pageable){
        return cargoRepository.findAll(pageable);
    }

    @Transactional
    public Cargo delete(Integer id){
        Cargo cargo = this.findById(id);
        cargoRepository.delete(cargo);
        return cargo;
    }

    public ApiResponse vincularCargoAoEgresso(Integer cargoId, Integer egressoId) {
        Egresso egresso = egressoRepository.findById(egressoId)
                .orElseThrow(() -> new ResourceNotFoundException("Egresso ID: " + egressoId));

        Cargo cargo = cargoRepository.findById(cargoId)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo ID: " + cargoId));

        cargo.setEgresso(egresso); // vincula o cargo ao egresso
        cargoRepository.save(cargo); // salva a atualização

        return new ApiResponse(true, "Cargo vinculado ao egresso com sucesso!");
    }

    @Transactional
    public String update(@Valid Cargo cargo){
        Cargo cargoObj = this.findById(cargo.getId());
        BeanUtils.copyProperties(cargo,cargoObj,"id");
        cargoRepository.save(cargoObj);
        return "Cargo atualizado com sucesso";
    }

}
