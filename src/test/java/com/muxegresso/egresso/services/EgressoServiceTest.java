package com.muxegresso.egresso.services;


import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.repositories.EgressoRepository;
import com.muxegresso.egresso.services.impl.EgressoServiceImpl;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        Egresso salvoEgresso = egressoServiceImpl.save(egresso);

        Integer id = salvoEgresso.getId();
        Egresso searchEgresso = egressoServiceImpl.findById(id);
        Assertions.assertEquals(salvoEgresso,searchEgresso);

    }
    */



}
