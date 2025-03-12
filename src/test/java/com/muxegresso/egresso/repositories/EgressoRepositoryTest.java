package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Egresso;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class EgressoRepositoryTest {

    @Autowired
    EgressoRepository egressoRepository;

    @Test
    @DisplayName("Verifica o salvamento de um egresso")
    public void deveVerificarSalvarEgresso(){
        EasyRandom easyRandom = new EasyRandom();
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(null);
        egresso.setEmail("email@testandoEgresso.com");
        Egresso salvoEgresso = egressoRepository.save(egresso);

        Assertions.assertNotNull(salvoEgresso);
        Assertions.assertEquals(egresso.getNome(), salvoEgresso.getNome());
        Assertions.assertEquals(egresso.getCpf(), salvoEgresso.getCpf());
        Assertions.assertEquals(egresso.getEmail(), salvoEgresso.getEmail());
        Assertions.assertEquals(egresso.getResumo(), salvoEgresso.getResumo());
        Assertions.assertEquals(egresso.getSenha(),salvoEgresso.getSenha());
        Assertions.assertEquals(egresso.getUrl_foto(), salvoEgresso.getUrl_foto());
    }

    @Test
    @DisplayName("Verifica o salvamento de um egresso")
    public void deveVerificarRemocaoEgresso(){
        EasyRandom easyRandom = new EasyRandom();
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(null);
        egresso.setEmail("email2@testandoEgresso.com");
        Egresso salvoEgresso = egressoRepository.save(egresso);
        Integer idEgresso = salvoEgresso.getId();

        egressoRepository.deleteById(idEgresso);

        Optional<Egresso> egressoFind = egressoRepository.findById(idEgresso);

        Assertions.assertFalse(egressoFind.isPresent());

    }

    @Test
    @DisplayName("Verifica a atualização de um Egresso")
    public void deveVerificarAtualizacaoDoEgresso(){
        EasyRandom easyRandom = new EasyRandom();
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(null);
        egresso.setEmail("email3@testandoEgresso.com");
        Egresso salvoEgresso = egressoRepository.save(egresso);
        salvoEgresso.setNome("nomeDiferente");

        egressoRepository.save(salvoEgresso);

        Egresso egressoNovo = egressoRepository.findById(salvoEgresso.getId()).orElseThrow();

        Assertions.assertNotNull(egressoNovo, "O egresso atualizado não deveria ser nulo.");
        Assertions.assertEquals("nomeDiferente", egressoNovo.getNome(), "O nome do curso não foi atualizado corretamente.");
        Assertions.assertEquals(egressoNovo.getCpf(), salvoEgresso.getCpf());
        Assertions.assertEquals(egressoNovo.getEmail(), salvoEgresso.getEmail());
        Assertions.assertEquals(egressoNovo.getResumo(), salvoEgresso.getResumo());
        Assertions.assertEquals(egressoNovo.getSenha(),salvoEgresso.getSenha());
        Assertions.assertEquals(egressoNovo.getUrl_foto(), salvoEgresso.getUrl_foto());

    }


}
