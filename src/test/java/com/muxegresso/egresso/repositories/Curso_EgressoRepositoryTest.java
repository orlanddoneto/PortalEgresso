package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Coordenador;
import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Curso_Egresso;
import com.muxegresso.egresso.domain.Egresso;
import jakarta.transaction.Transactional;
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

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    CoordenadorRepository coordenadorRepository;

    @Autowired
    EgressoRepository egressoRepository;


    @Test
    @DisplayName("Verifica o salvamento de um Curso_Egresso")
    @Transactional
    public void deveVerificarSalvarCursoEgresso(){
        EasyRandom easyRandom = new EasyRandom();
        // Cria um curso válido
        Curso cursoValido = easyRandom.nextObject(Curso.class);
        cursoValido.setId(null);
        cursoValido.setNome("Curso Exemplo");
        cursoValido = cursoRepository.save(cursoValido);

        Curso_Egresso cursoEgresso = easyRandom.nextObject(Curso_Egresso.class);
        cursoEgresso.setId(null);
        cursoEgresso.setCurso(cursoValido);

        Curso_Egresso ceSalvo = cursoEgressoRepository.save(cursoEgresso);

        Assertions.assertNotNull(ceSalvo);
        Assertions.assertEquals(cursoEgresso.getAno_fim(),ceSalvo.getAno_fim());
        Assertions.assertEquals(cursoEgresso.getAno_inicio(), ceSalvo.getAno_inicio());

    }

    @Test
    @DisplayName("Verifica a deleção de um Curso_Egresso")
    public void deveVerificarDelecaoCursoEgresso(){
        EasyRandom easyRandom = new EasyRandom();
        // Cria um curso válido
        Curso cursoValido = easyRandom.nextObject(Curso.class);
        cursoValido.setId(null);
        cursoValido.setNome("Curso Exemplo");


        // Cria um coordenador válido
        Coordenador coordenadorValido = easyRandom.nextObject(Coordenador.class);
        coordenadorValido.setId(null);
        coordenadorValido.setNome("Coordenador Exemplo");

        coordenadorValido.getCursos().add(cursoValido);
        cursoValido.setCoordenador(coordenadorValido);

        coordenadorValido = coordenadorRepository.save(coordenadorValido);
        cursoValido = cursoRepository.save(cursoValido);

        Curso_Egresso cursoEgresso = easyRandom.nextObject(Curso_Egresso.class);
        cursoEgresso.setId(null);
        cursoEgresso.setCurso(cursoValido);
        cursoEgresso.setEgresso(null);

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

        Curso cursoValido = easyRandom.nextObject(Curso.class);
        cursoValido.setId(null);
        cursoValido.setNome("Curso Exemplo");

        // Cria um coordenador válido
        Coordenador coordenadorValido = easyRandom.nextObject(Coordenador.class);
        coordenadorValido.setId(null);
        coordenadorValido.setNome("Coordenador Exemplo");

        coordenadorValido.getCursos().add(cursoValido);
        cursoValido.setCoordenador(coordenadorValido);

        coordenadorValido = coordenadorRepository.save(coordenadorValido);
        cursoValido=cursoRepository.save(cursoValido);

        // Cria e salva um Egresso válido
        Egresso egressoValido = easyRandom.nextObject(Egresso.class);
        egressoValido.setId(null);
        egressoValido.setNome("Exemplo egresso");
        egressoValido.setEmail("exemploEmail");
        egressoValido = egressoRepository.save(egressoValido);


        Curso_Egresso cursoEgresso = easyRandom.nextObject(Curso_Egresso.class);
        cursoEgresso.setId(null);
        cursoEgresso.setCurso(cursoValido);
        cursoEgresso.setEgresso(egressoValido);

        Curso_Egresso salvoCE = cursoEgressoRepository.save(cursoEgresso);
        salvoCE.setAno_fim(2024);
        Curso_Egresso atualizadoCE = cursoEgressoRepository.save(salvoCE);

        Assertions.assertNotNull(atualizadoCE);
        Assertions.assertEquals(atualizadoCE.getAno_fim(),2024);
        Assertions.assertEquals(atualizadoCE.getAno_inicio(),cursoEgresso.getAno_inicio());

    }

}
