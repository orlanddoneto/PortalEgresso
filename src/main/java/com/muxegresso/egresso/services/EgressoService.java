package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.*;
import com.muxegresso.egresso.repositories.CargoRepository;
import com.muxegresso.egresso.repositories.CursoRepository;
import com.muxegresso.egresso.repositories.DepoimentoRepository;
import com.muxegresso.egresso.repositories.EgressoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EgressoService {
    @Autowired
    EgressoRepository egressoRepository;



    public String efetuarLogin(String email, String senha){
        Egresso egresso = egressoRepository.FindByEmail(email).orElseThrow(() -> new RuntimeException("Email não existente na base de dados."));
        if (egresso.getSenha().equals(senha)) return "Login efetuado com sucesso.";
        return "Senha incorreta";
    }

    @Transactional
    public Egresso save(@Valid Egresso egresso){
        return egressoRepository.save(egresso);
    }

    public Egresso findById( Integer id){
        Egresso egresso = egressoRepository.findById(id).orElseThrow(()->new RuntimeException("ID não presente no sistema."));
        return egresso;
    }

    public Page<Egresso> findAll(Pageable pageable){
        return egressoRepository.findAll(pageable);
    }

    @Transactional
    public Egresso delete(Integer id){
        Egresso egresso = this.findById(id);
        egressoRepository.delete(egresso);
        return egresso;
    }

    @Transactional
    public String update(@Valid Egresso egresso){
        Egresso egressoObj = this.findById(egresso.getId());
        if(egresso.getSenha().equals(egressoObj.getSenha())) {
            egressoRepository.save(egressoObj);
            return "Egresso atualizado com sucesso";
        }
        return "Erro ao atualizar egresso.";
    }




}
