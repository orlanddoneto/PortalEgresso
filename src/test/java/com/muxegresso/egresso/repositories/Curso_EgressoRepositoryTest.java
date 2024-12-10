package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Curso_Egresso;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class Curso_EgressoRepositoryTest {

    @Autowired
    Curso_EgressoRepository cursoEgressoRepository;

    @Test
    @DisplayName("Verifica o salvamento de um Curso_Egresso")
    public void deveVerificarSalvarCursoEgresso(){
        EasyRandom easyRandom = new EasyRandom();
        Curso_Egresso cursoEgresso = easyRandom.nextObject(Curso_Egresso.class);
        cursoEgresso.setId(null);

        Curso_Egresso ceSalvo = cursoEgressoRepository.save(cursoEgresso);

        Assertions.assertNotNull(ceSalvo);
        Assertions.assertEquals(cursoEgresso.getAno_fim(),ceSalvo.getAno_fim());
        Assertions.assertEquals(cursoEgresso.getAno_inicio(), ceSalvo.getAno_inicio());

    }

    @Test
    @DisplayName("Verifica a deleção de um Curso_Egresso")
    public void deveVerificarDelecaoCursoEgresso(){
        EasyRandom easyRandom = new EasyRandom();
        Curso_Egresso cursoEgresso = easyRandom.nextObject(Curso_Egresso.class);
        cursoEgresso.setId(null);

        Curso_Egresso ceSalvo = cursoEgressoRepository.save(cursoEgresso);
        Integer idCE = ceSalvo.getId();

        cursoEgressoRepository.deleteById(idCE);

        Optional<Curso_Egresso> ceFind = cursoEgressoRepository.findById(idCE);
         Assertions.assertFalse(ceFind.isPresent());
    }

    @Test
    @DisplayName("Verifica a atualização de um Curso_Egresso")
    public void deveVerificarAtualizacaoCursoEgresso(){
        EasyRandom easyRandom = new EasyRandom();
        Curso_Egresso cursoEgresso = easyRandom.nextObject(Curso_Egresso.class);
        cursoEgresso.setId(null);

        Curso_Egresso salvoCE = cursoEgressoRepository.save(cursoEgresso);
        salvoCE.setAno_fim(2024);
        Curso_Egresso atualizadoCE = cursoEgressoRepository.save(salvoCE);

        Assertions.assertNotNull(atualizadoCE);
        Assertions.assertEquals(atualizadoCE.getAno_fim(),2024);
        Assertions.assertEquals(atualizadoCE.getAno_inicio(),cursoEgresso.getAno_inicio());

    }

}
