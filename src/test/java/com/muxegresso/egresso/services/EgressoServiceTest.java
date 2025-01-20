package com.muxegresso.egresso.services;


import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.dtos.RequestEgressoDto;
import com.muxegresso.egresso.repositories.EgressoRepository;
import com.muxegresso.egresso.services.impl.EgressoServiceImpl;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class EgressoServiceTest {

    @Mock
    private EgressoRepository egressoRepositoryMock; // Mock do repositório

    @InjectMocks
    private EgressoServiceImpl egressoServiceImpl; // Serviço com o mock injetado


    ModelMapper modelMapper = new ModelMapper();

    @Test
    @DisplayName("Verifica o salvamento de um egresso")
    public void deveTestarSalvarEgresso(){
        EasyRandom easyRandom = new EasyRandom();
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(null);

        var egressoEntity = modelMapper.map(egresso, RequestEgressoDto.class);

        Egresso salvoEgresso = egressoServiceImpl.save(egressoEntity);

    public Egresso criaEgresso() {
        EasyRandom easyRandom = new EasyRandom();
        return easyRandom.nextObject(Egresso.class);
    }

    @Test
    @DisplayName("Verifica a busca de todos os egressos")
    public void deveTestarFindAllEgresso() {


    }

    /*
    @Test
    @DisplayName("Verifica a busca por ID de um egresso")
    public void deveTestarFindByID(){
        EasyRandom easyRandom = new EasyRandom();
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(null);

        var egressoEntity = modelMapper.map(egresso, RequestEgressoDto.class);
        Egresso salvoEgresso = egressoServiceImpl.save(egressoEntity);

        Integer id = salvoEgresso.getId();
        Egresso searchEgresso = egressoServiceImpl.findById(id).get();
        Assertions.assertEquals(salvoEgresso,searchEgresso);

    }


    */



}
