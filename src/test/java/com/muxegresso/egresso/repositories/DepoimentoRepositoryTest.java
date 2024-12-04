package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.domain.Egresso;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class DepoimentoRepositoryTest {
    @Autowired
    DepoimentoRepository depoimentoRepository;

    @Autowired
    EgressoRepository egressoRepository;

    @Test
    @DisplayName("Verifica o salvamento de um depoimento")
    public void deveVerificarSalvarDepoimento(){
        // Gera a entidade automaticamente
        EasyRandom easyRandom = new EasyRandom();
        Depoimento depoimento = easyRandom.nextObject(Depoimento.class);
        Egresso egresso = easyRandom.nextObject(Egresso.class);

        //Alterações necessárias para que o objeto seja criado randomicamente de forma correta
        depoimento.setEgresso(egresso);
        egresso.setId(null);
        depoimento.setId(null);

        egressoRepository.save(egresso);
        Depoimento salvoDepoimento = depoimentoRepository.save(depoimento);

        Assertions.assertNotNull(salvoDepoimento);
        Assertions.assertEquals(depoimento.getTexto(), salvoDepoimento.getTexto());
        Assertions.assertEquals(depoimento.getData(), salvoDepoimento.getData());

    }

    @Test
    @DisplayName("Verifica a deleção de um depoimento")
    public void deveVerificarDeletarDepoimento(){
        // Gera a entidade automaticamente
        EasyRandom easyRandom = new EasyRandom();
        Depoimento depoimento = easyRandom.nextObject(Depoimento.class);

        //Alterações necessárias para que o objeto seja criado randomicamente de forma correta
        depoimento.setEgresso(null);
        depoimento.setId(null);

        Depoimento salvoDepoimento = depoimentoRepository.save(depoimento);
        depoimentoRepository.deleteById(salvoDepoimento.getId());

        Optional<Depoimento> temp = depoimentoRepository.findById(salvoDepoimento.getId());
        Assertions.assertFalse(temp.isPresent());
    }

    @Test
    @DisplayName("Verifica a atualização da descrição de um depoimento")
    public void deveVerificarAtualizacaoDepoimento(){
        EasyRandom easyRandom = new EasyRandom();

        // Gera um egresso e salva
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(null);
        Egresso salvoEgresso = egressoRepository.save(egresso);

        // Gera a depoimento automaticamente
        Depoimento depoimento = easyRandom.nextObject(Depoimento.class);

        //Alterações necessárias para que o objeto seja criado randomicamente de forma correta
        depoimento.setEgresso(salvoEgresso);
        depoimento.setId(null);

        Depoimento salvoDepoimento = depoimentoRepository.save(depoimento);
        salvoDepoimento.setTexto("Atualizado");

        Depoimento atualizadoDepoimento = depoimentoRepository.save(salvoDepoimento);

        Assertions.assertNotNull(atualizadoDepoimento);
        Assertions.assertEquals("Atualizado", atualizadoDepoimento.getTexto());
    }

}
