package com.muxegresso.egresso.services.impl;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.Oportunidade;
import com.muxegresso.egresso.domain.dtos.SendOportunidadeDto;
import com.muxegresso.egresso.domain.enums.AproveStatus;
import com.muxegresso.egresso.infra.security.TokenService;
import com.muxegresso.egresso.repositories.EgressoRepository;
import com.muxegresso.egresso.repositories.OportunidadeRepository;
import com.muxegresso.egresso.services.OportunidadeService;
import com.muxegresso.egresso.services.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OportunidadeImpl implements OportunidadeService {
    @Autowired
    OportunidadeRepository oportunidadeRepository;

    @Autowired
    private EgressoRepository egressoRepository;

    @Autowired
    private TokenService tokenService;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public Oportunidade save(Oportunidade oportunidade) {
        oportunidade.setHomologadoStatus(AproveStatus.PENDING);
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
    public ApiResponse homologarOportunidade(Integer id, String token, String status) {
        String role = tokenService.getRoleFromToken(token);

        if (!"COORDENADOR_ROLE".equals(role)) {
            return new ApiResponse(false, "Erro: apenas administradores podem homologar oportunidades.");
        }

        AproveStatus novoStatus;
        try {
            novoStatus = AproveStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ApiResponse(false, "Status inválido: " + status);
        }

        Oportunidade oportunidade = this.findById(id);

        if (oportunidade.getHomologadoStatus() == novoStatus) {
            return new ApiResponse(false, "Oportunidade já está com status: " + novoStatus);
        }

        oportunidade.setHomologadoStatus(novoStatus);
        oportunidadeRepository.save(oportunidade);

        return new ApiResponse(true, "Status da oportunidade atualizado para: " + novoStatus);
    }

    @Override
    public ApiResponse criarOportunidade(SendOportunidadeDto request) {
        Egresso egresso = egressoRepository.findById(request.getEgressoId())
                .orElseThrow(() -> new ResourceNotFoundException(request.getEgressoId()));

        Oportunidade oportunidade = new Oportunidade();
        oportunidade.setEgresso(egresso);
        oportunidade.setTitulo(request.getTitulo());
        oportunidade.setLink(request.getLink());
        oportunidade.setCreatedAt(LocalDateTime.now());
        oportunidade.setDescricao(request.getDescricao());
        oportunidade.setHomologadoStatus(AproveStatus.PENDING);

        oportunidadeRepository.save(oportunidade);

        return new ApiResponse(true, "Oportunidade enviada com sucesso!");
    }

    @Override
    public Page<Oportunidade> listarPendentesPorEgressoId(
            Pageable pageable, Integer egressoId, String titulo) {

        if (titulo == null || titulo.trim().isEmpty()) {
            return oportunidadeRepository.findByHomologadoStatusAndEgressoId(
                    AproveStatus.PENDING, egressoId, pageable);
        }

        return oportunidadeRepository.findByHomologadoStatusAndEgressoIdAndTituloContainingIgnoreCase(
                AproveStatus.PENDING, egressoId, titulo.trim(), pageable);
    }

    @Override
    public Page<SendOportunidadeDto> findAllByEgressoId(Pageable pageable, Integer id) {
        return oportunidadeRepository.findAllByEgressoId(pageable, id).map(oportunidade -> modelMapper.map(oportunidade, SendOportunidadeDto.class));
    }

    @Override
    public Page<Oportunidade> listarTodosPendentes(Pageable pageable, String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return oportunidadeRepository.findByHomologadoStatus(AproveStatus.PENDING, pageable);
        }

        return oportunidadeRepository.findByHomologadoStatusAndTituloContainingIgnoreCase(
                AproveStatus.PENDING, titulo.trim(), pageable);
    }


}
