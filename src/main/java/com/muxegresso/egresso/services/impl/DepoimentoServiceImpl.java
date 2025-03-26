package com.muxegresso.egresso.services.impl;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.dtos.RequestDepoimentoDto;
import com.muxegresso.egresso.domain.enums.AproveStatus;
import com.muxegresso.egresso.domain.enums.StatusDepoimento;
import com.muxegresso.egresso.infra.security.TokenService;
import com.muxegresso.egresso.repositories.CoordenadorRepository;
import com.muxegresso.egresso.repositories.DepoimentoRepository;
import com.muxegresso.egresso.repositories.EgressoRepository;
import com.muxegresso.egresso.services.DepoimentoService;
import com.muxegresso.egresso.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Service
public class DepoimentoServiceImpl implements DepoimentoService {
    @Autowired
    DepoimentoRepository depoimentoRepository;

    private ModelMapper modelMappper = new ModelMapper();

    @Autowired
    private EgressoRepository egressoRepository;

    @Autowired
    CoordenadorRepository coordenadorRepository;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public Depoimento save(@Valid RequestDepoimentoDto depoimento){
        Depoimento depoimentoEntity = modelMappper.map(depoimento, Depoimento.class);
        depoimentoEntity.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        depoimentoEntity.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        depoimentoEntity.setHomologadoStatus(AproveStatus.PENDING);
        return depoimentoRepository.save(depoimentoEntity);
    }

    public ApiResponse enviarDepoimento(RequestDepoimentoDto request) {
        Egresso egresso = egressoRepository.findById(request.getEgressoId())
                .orElseThrow(() -> new ResourceNotFoundException(request.getEgressoId()));

        Depoimento depoimento = new Depoimento();
        depoimento.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        depoimento.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        depoimento.setEgresso(egresso);
        depoimento.setTexto(request.getTexto());
        depoimento.setData(new Date());
        depoimento.setHomologadoStatus(AproveStatus.PENDING);

        depoimentoRepository.save(depoimento);

        return new ApiResponse(true, "Depoimento enviado com sucesso!");
    }

    @Override
    public Page<RequestDepoimentoDto> findAllByEgressoId(Pageable pageable, Integer id) {
        return depoimentoRepository.findAllByEgressoId(pageable, id).map(depoimento -> modelMappper.map(depoimento, RequestDepoimentoDto.class));
    }

    @Override
    public Page<Depoimento> listarPendentesPorEgressoId(Pageable pageable, Integer idEgresso, String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return depoimentoRepository.findByHomologadoStatusAndEgressoId(
                    AproveStatus.PENDING, idEgresso, pageable);
        }

        return depoimentoRepository.findByHomologadoStatusAndEgressoIdAndTextoContainingIgnoreCase(
                AproveStatus.PENDING, idEgresso, texto.trim(), pageable);
    }

    @Override
    public Page<Depoimento> listarTodosPendentes(Pageable pageable, String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return depoimentoRepository.findByHomologadoStatus(AproveStatus.PENDING, pageable);
        }

        return depoimentoRepository.findByHomologadoStatusAndTextoContainingIgnoreCase(
                AproveStatus.PENDING, texto.trim(), pageable);
    }

    public Depoimento findById( Integer id){
        Depoimento depoimento = depoimentoRepository.findById(id).orElseThrow(()->new RuntimeException("ID não presente no sistema."));
        return depoimento;
    }

    public Page<Depoimento> findAll(Pageable pageable){
        return depoimentoRepository.findAll(pageable);
    }

    @Transactional
    public Depoimento delete(Integer id){
        Depoimento depoimento = this.findById(id);
        depoimentoRepository.delete(depoimento);
        return depoimento;
    }

    @Transactional
    public ApiResponse update(@Valid Depoimento depoimento){
        Depoimento depoimentoObj = this.findById(depoimento.getId());
        BeanUtils.copyProperties(depoimento,depoimentoObj,"id");
        depoimentoRepository.save(depoimento);
        return new ApiResponse(true,"Depoimento atualizado com sucesso");
    }

    public Page<Depoimento> buscarPorData(Integer ano, Pageable pageable) {
        return depoimentoRepository.findAllByAno(ano, pageable);
    }

    @Override
    public ApiResponse alterarStatus(Integer id, StatusDepoimento novoStatus) {
        Depoimento depoimento = depoimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Depoimento não encontrado"));

        depoimento.setHomologadoStatus(AproveStatus.valueOf(novoStatus.toString().toUpperCase()));
        return new ApiResponse(true,"Status do depoimento alterado com sucesso!");
    }

    @Override
    public ApiResponse homologarDepoimento(Integer id, String token, String status) {
        String role = tokenService.getRoleFromToken(token);

        if (!"COORDENADOR_ROLE".equals(role)) {
            return new ApiResponse(false, "Erro: apenas coordenadores podem homologar depoimentos.");
        }

        AproveStatus novoStatus;
        try {
            novoStatus = AproveStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ApiResponse(false, "Status inválido: " + status);
        }

        Depoimento depoimento = this.findById(id);

        if (depoimento.getHomologadoStatus() == novoStatus) {
            return new ApiResponse(false, "Depoimento já está com status: " + novoStatus);
        }

        depoimento.setHomologadoStatus(novoStatus);
        depoimentoRepository.save(depoimento);

        return new ApiResponse(true, "Status do depoimento atualizado para: " + novoStatus);
    }

    public  Page<Depoimento> buscarRecentes(Integer dias, Pageable pageable){
        return depoimentoRepository.findDepoimentosRecentes(dias,pageable);
    }
}
