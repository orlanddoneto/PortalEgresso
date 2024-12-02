package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Curso;
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
    public void deveVerificarAtualizacaoNomeCurso(){
        Curso curso = new Curso(null,"testeAttNome","testeNivel");
        Curso salvoCurso = cursoRepository.save(curso);

        curso.setNome("novoNome");

        salvoCurso=cursoRepository.save(curso);
        Curso cursoAtualizado = cursoRepository.findById(salvoCurso.getId()).orElseThrow();

        Assertions.assertNotNull(cursoAtualizado);
        Assertions.assertEquals(curso.getNome(), cursoAtualizado.getNome());

    }


}
