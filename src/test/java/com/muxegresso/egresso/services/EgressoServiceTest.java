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
    public void deveTestarSalvarEgresso(){
        EasyRandom easyRandom = new EasyRandom();
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(null);
        Egresso salvoEgresso = egressoService.save(egresso);

        Assertions.assertNotNull(salvoEgresso);
        Assertions.assertEquals(egresso.getNome(), salvoEgresso.getNome());
        Assertions.assertEquals(egresso.getCpf(), salvoEgresso.getCpf());
        Assertions.assertEquals(egresso.getEmail(), salvoEgresso.getEmail());
        Assertions.assertEquals(egresso.getResumo(), salvoEgresso.getResumo());
        Assertions.assertEquals(egresso.getSenha(),salvoEgresso.getSenha());
        Assertions.assertEquals(egresso.getUrl_foto(), salvoEgresso.getUrl_foto());
    }

    @Test
    @DisplayName("Verifica a busca por ID de um egresso")
    public void deveTestarFindByID(){
        EasyRandom easyRandom = new EasyRandom();
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(null);
        Egresso salvoEgresso = egressoService.save(egresso);

        Integer id = salvoEgresso.getId();
        Egresso searchEgresso = egressoService.findById(id);
        Assertions.assertEquals(salvoEgresso,searchEgresso);

    }

    @Test
    @DisplayName("Verifica a busca todos os egressos")
    public void deveTestarFindAll(){

    }



}
