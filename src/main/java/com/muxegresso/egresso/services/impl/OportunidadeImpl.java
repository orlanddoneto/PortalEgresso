package com.muxegresso.egresso.services.impl;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.domain.Oportunidade;
import com.muxegresso.egresso.domain.dtos.UsuarioDTO;
import com.muxegresso.egresso.domain.enums.UserTipo;
import com.muxegresso.egresso.repositories.OportunidadeRepository;
import com.muxegresso.egresso.services.OportunidadeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OportunidadeImpl implements OportunidadeService {
    @Autowired
    OportunidadeRepository oportunidadeRepository;

    @Override
    public Oportunidade save(Oportunidade oportunidade) {
        return oportunidadeRepository.save(oportunidade);
    }

    @Override
    public Oportunidade findById(Integer id) {
        return oportunidadeRepository.findById(id).orElseThrow(()->new RuntimeException("ID não presente no sistema."));
    }

    @Override
    public Page<Oportunidade> findAll(Pageable pageable) {
        return oportunidadeRepository.findAll(pageable);
    }

    @Override
    public Oportunidade delete(Integer id) {
        Oportunidade oportunidade = this.findById(id);
        oportunidadeRepository.delete(oportunidade);
        return oportunidade;
    }

    @Override
    public ApiResponse update(Oportunidade oportunidade) {
        Oportunidade oport_temp = this.findById(oportunidade.getId());
        BeanUtils.copyProperties(oportunidade,oport_temp,"id");
        oportunidadeRepository.save(oport_temp);
        return new ApiResponse(true, "Oportunidade atualizada!");
    }

    @Override
    public Page<Oportunidade> buscarRecentes(Integer dias, Pageable pageable) {
        return oportunidadeRepository.findOportunidadesRecentes(dias,pageable);
    }

    @Override
    public Page<Oportunidade> buscarPorAno(Integer ano, Pageable pageable) {
        return oportunidadeRepository.findAllByAno(ano,pageable);
    }

    @Override
    public ApiResponse homologarOportunidade(Integer id, UsuarioDTO usuarioDTO) {
        if(usuarioDTO.getTipo().equals(UserTipo.Coordenador)){
            Oportunidade oportunidade = this.findById(id);
            if(!oportunidade.isHomologado()){
                oportunidade.setHomologado(true);
                oportunidadeRepository.save(oportunidade);
                return new ApiResponse(true, "Oportunidade homologada com sucesso!");
            }
        }
        return new ApiResponse(false, "Erro ao homologar o oportunidade, é necessário ser um coordenador!");
    }
}
