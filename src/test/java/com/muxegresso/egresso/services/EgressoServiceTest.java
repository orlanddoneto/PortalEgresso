package com.muxegresso.egresso.services;


import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.repositories.EgressoRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EgressoServiceTest {

    @Autowired
    EgressoRepository egressoRepository;

    @Autowired
    EgressoService egressoService;

    @Test
    @DisplayName("Verifica o salvamento de um egresso")
    public void deveTestarEfetuarLogin(){


    }


}
