package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Coordenador;
import com.muxegresso.egresso.domain.Curso;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CursoRepositoryTest {
    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    CoordenadorRepository coordenadorRepository;

    @Test
    @DisplayName("Verifica o salvamento de um curso")
    public void deveVerificarSalvarCurso(){
        Curso curso = new Curso(null,"testeNome","testeNivel");

        Curso salvoCurso = cursoRepository.save(curso);

        Assertions.assertNotNull(salvoCurso);
        Assertions.assertEquals(curso.getNome(), salvoCurso.getNome());
        Assertions.assertEquals(curso.getNivel(), salvoCurso.getNivel());
    }

    @Test
    @DisplayName("Verifica a remoção de um curso")
    public void deveVerificarRemoverCurso(){
        Curso curso = new Curso(null,"testeNome","testeNivel");

        Curso salvoCurso = cursoRepository.save(curso);
        Integer id = salvoCurso.getId();
        cursoRepository.deleteById(salvoCurso.getId());

        Optional<Curso> temp = cursoRepository.findById(id);
        Assertions.assertFalse(temp.isPresent());
    }

    @Test
    @DisplayName("Verifica a atualização do nome do curso")
    public void deveVerificarAtualizacaoNomeCurso() {
        // Gera um curso usando EasyRandom
        EasyRandom easyRandom = new EasyRandom();
        Curso curso = easyRandom.nextObject(Curso.class);

        // Ajusta valores que não devem ser gerados automaticamente
        curso.setId(null);

        // Cria um coordenador válido
        Coordenador coordenadorValido = easyRandom.nextObject(Coordenador.class);
        coordenadorValido.setId(null);
        coordenadorValido.setNome("Coordenador Exemplo");

        coordenadorValido.getCursos().add(curso);
        curso.setCoordenador(coordenadorValido);

        coordenadorValido = coordenadorRepository.save(coordenadorValido);
        Curso salvoCurso=cursoRepository.save(curso);


        // Atualiza o nome do curso e salva novamente
        salvoCurso.setNome("novoNome");
        cursoRepository.save(salvoCurso);

        // Recupera o curso atualizado do banco
        Curso cursoAtualizado = cursoRepository.findById(salvoCurso.getId()).orElseThrow();

        // Validações
        Assertions.assertNotNull(cursoAtualizado, "O curso atualizado não deveria ser nulo.");
        Assertions.assertEquals("novoNome", cursoAtualizado.getNome(), "O nome do curso não foi atualizado corretamente.");
        Assertions.assertEquals(cursoAtualizado.getNivel(), salvoCurso.getNivel());

    }



}
