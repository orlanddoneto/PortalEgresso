package com.muxegresso.egresso.services.impl;

import com.muxegresso.egresso.domain.*;
import com.muxegresso.egresso.repositories.*;
import com.muxegresso.egresso.services.CoordenadorService;
import jakarta.validation.Valid;
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

    public String efetuarLogin(String email, String senha){
        Coordenador coordenador = coordenadorRepository.FindByEmail(email).orElseThrow(() -> new RuntimeException("Email não existente na base de dados."));
        if (coordenador.getSenha().equals(senha)) return "Login efetuado com sucesso.";
        return "Senha incorreta";
    }

    @Transactional
    public Coordenador save(@Valid Coordenador coordenador){
        return coordenadorRepository.save(coordenador);
    }

    public Coordenador findById( Integer id){
        Coordenador coordenador = coordenadorRepository.findById(id).orElseThrow(()->new RuntimeException("ID não presente no sistema."));
        return coordenador;
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
