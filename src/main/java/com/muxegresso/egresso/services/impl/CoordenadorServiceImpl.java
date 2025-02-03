package com.muxegresso.egresso.services.impl;

import com.muxegresso.egresso.domain.*;
import com.muxegresso.egresso.repositories.*;
import com.muxegresso.egresso.services.CoordenadorService;
import com.muxegresso.egresso.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoordenadorServiceImpl implements CoordenadorService {
    @Autowired
    CoordenadorRepository coordenadorRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    EgressoRepository egressoRepository;

    @Autowired
    CargoRepository cargoRepository;

    @Autowired
    DepoimentoRepository depoimentoRepository;

    public boolean efetuarLogin(String email, String senha){
        Coordenador coordenador = coordenadorRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email não existente na base de dados."));
        if (coordenador.getSenha().equals(senha)) return true;
        return false;
    }

    @Transactional
    public Coordenador save(@Valid Coordenador coordenador){
        return coordenadorRepository.save(coordenador);
    }

    public Coordenador findById( Integer id){
        Coordenador coordenador = coordenadorRepository.findById(id).orElseThrow(()->new RuntimeException("ID não presente no sistema."));
        return coordenador;
    }

    @Override
    public Depoimento lincarDepoimentoEgresso(@NotNull Egresso egresso, @NotNull Depoimento depoimento) {
        depoimento.setEgresso(egresso);
        return depoimentoRepository.save(depoimento);
    }

    @Override
    public Curso addCurso(@NotNull String email, @NotBlank String nome, @NotBlank String nivel) {
        Coordenador coordenador = coordenadorRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException(email));

        Curso curso = new Curso();

        curso.setCoordenador(coordenador);
        curso.setNome(nome);
        curso.setNivel(nivel);

        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public Cargo addCargo(@NotNull Egresso egresso,@NotNull Cargo cargo) {
        cargo.setEgresso(egresso);
        return cargoRepository.save(cargo);
    }

    public Page<Coordenador> findAll(Pageable pageable){
        return coordenadorRepository.findAll(pageable);
    }

    public Coordenador delete(Integer id){
        Coordenador coordenador = this.findById(id);
        coordenadorRepository.delete(coordenador);
        return coordenador;
    }

    public String update(@Valid Coordenador coordenador){
        Coordenador coordenadorObj = this.findById(coordenador.getId());
        if(coordenador.getSenha().equals(coordenadorObj.getSenha())) {
            coordenadorRepository.save(coordenadorObj);
            return "Coordenador atualizado com sucesso";
        }
        return "Erro ao atualizar coordenador.";
    }

    @Transactional
    public void RegistrarEgresso(Egresso egresso){
        egressoRepository.save(egresso);
    }

    @Transactional
    public Curso registrarCurso(@Valid Curso curso, @Valid Coordenador coordenador){
        curso.setCoordenador(coordenador);
        return cursoRepository.save(curso);
    }

    @Transactional
    public void AdicionarCargo(@Valid Egresso egresso, @Valid Cargo cargo){
        cargo.setEgresso(egresso);
        cargoRepository.save(cargo);
    }

    @Transactional
    public void AdicionarDepoimento(@Valid Egresso egresso, @Valid Depoimento depoimento){
        depoimento.setEgresso(egresso);
        depoimentoRepository.save(depoimento);
    }


}
