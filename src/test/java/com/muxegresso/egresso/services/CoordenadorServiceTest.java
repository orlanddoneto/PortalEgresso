package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.*;
import com.muxegresso.egresso.repositories.CoordenadorRepository;
import com.muxegresso.egresso.repositories.EgressoRepository;
import com.muxegresso.egresso.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class CoordenadorServiceTest {

    @Autowired
    CoordenadorService coordenadorService;

    @Autowired
    CoordenadorRepository coordenadorRepository;

    @Autowired
    EgressoRepository egressoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarSemLogin() {
        Coordenador coordenador = new Coordenador();
        coordenador.setSenha("mux123");
        coordenador.setTipo("flip");

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.save(coordenador), "O dados de login devem ser preenchidos!");
        Assertions.assertEquals("O dados de login devem ser preenchidos!", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarSemSenha() {
        Coordenador coordenador = new Coordenador();
        coordenador.setEmail("login teste");
        coordenador.setTipo("flip");

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.save(coordenador), "Defina uma senha para o coordenador!");
        Assertions.assertEquals("Defina uma senha para o coordenador!", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarSemTipo() {
        Coordenador coordenador = new Coordenador();
        coordenador.setEmail("login teste");
        coordenador.setSenha("mux123");

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.save(coordenador), "O tipo do coordenador deve estar preenchido");
        Assertions.assertEquals("O tipo do coordenador deve estar preenchido", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarLoginJaCadastrado() {
        Coordenador coordenador = new Coordenador();
        coordenador.setEmail("coordenador1");
        coordenador.setSenha("mux123");
        coordenador.setTipo("flip");

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.save(coordenador), "O login já existe, por favor tente um diferente!");
        Assertions.assertEquals("O login já existe, por favor tente um diferente!", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarAtualizarSemLogin() {
        Coordenador coordenador = coordenadorRepository.findById(2001).orElseThrow(()-> new ResourceNotFoundException(2001));
        assert coordenador != null;

        coordenador.setEmail(null);
        coordenador.setSenha("mux123");
        coordenador.setTipo("flip");

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.update(coordenador), "O dados de login devem ser preenchidos!");
        Assertions.assertEquals("O dados de login devem ser preenchidos!", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarAtualizarSemSenha() {
        Coordenador coordenador = coordenadorRepository.findById(2001).orElseThrow(()-> new ResourceNotFoundException(2001));
        assert coordenador != null;

        coordenador.setSenha(null);
        coordenador.setEmail("login teste");
        coordenador.setTipo("flip");

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.update(coordenador), "Defina uma senha para o coordenador!");
        Assertions.assertEquals("Defina uma senha para o coordenador!", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarAtualizarSemTipo() {
        Coordenador coordenador = coordenadorRepository.findById(2001).orElseThrow(()-> new ResourceNotFoundException(2001));
        assert coordenador != null;

        coordenador.setTipo(null);
        coordenador.setEmail("login teste");
        coordenador.setSenha("mux123");

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.update(coordenador), "O tipo do coordenador deve estar preenchido");
        Assertions.assertEquals("O tipo do coordenador deve estar preenchido", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarAtualizarLoginJaCadastrado() {
        Coordenador coordenadorSalvo = entityManager
                .createQuery("SELECT c FROM Coordenador c WHERE c.id = :id", Coordenador.class)
                .setParameter("id", 2001)
                .getSingleResult();

        coordenadorSalvo.setEmail("coordenador2");
        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.update(coordenadorSalvo), "O login já existe, por favor tente um diferente!");
        Assertions.assertEquals("O login já existe, por favor tente um diferente!", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoAtualizarCoordenadorNaoExistente() {
        Coordenador coordenador = new Coordenador();
        coordenador.setId(2001);
        coordenador.setEmail("login teste");
        coordenador.setSenha("mux123");
        coordenador.setTipo("flip");

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.update(coordenador), "ID não encontrado");
        Assertions.assertEquals("ID não encontrado", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoBuscarPorIdVazio() {
        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.findById(null), "ID inválido");
        Assertions.assertEquals("ID inválido", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoBuscarPorIdInexistente() {
        Integer idInexistente = 1812;

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.findById(idInexistente), "ID não encontrado");
        Assertions.assertEquals("ID não encontrado", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroQuandoLoginIncorreto() {
        Exception exception = Assertions.assertThrows(Exception.class,
                () -> coordenadorService.efetuarLogin("loginIncorreto", "senhaIncorreta"),
                "Erro de autenticação");
        Assertions.assertEquals("Erro de autenticação", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoCadastrarCursoComCoordenadorInvalido() {
        Coordenador coordenador = new Coordenador();
        coordenador.setEmail("loginInvalido");
        coordenador.setSenha("mux123");
        coordenador.setTipo("flip");

        Exception exception = Assertions.assertThrows(Exception.class,
                () -> coordenadorService.addCurso(coordenador.getEmail(), "Ciência da Computação", "nivel teste"),
                "Login inválido");

        Assertions.assertEquals("Login inválido", exception.getMessage());
    }


    @Test
    @Transactional
    public void deveGerarErroAoTentarAssociarCargoInvalidoAEgresso() {
        Egresso egresso = egressoRepository.findById(2001).orElseThrow(()-> new ResourceNotFoundException(2001));
        assert egresso != null;

        Cargo cargo = new Cargo();
        cargo.setEgresso(egresso);

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.addCargo(egresso, cargo), "Cargo inválido");
        Assertions.assertEquals("Cargo inválido", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveCadastrarCurso() {
        Coordenador coordenador = coordenadorRepository.findById(2001).orElseThrow(()-> new ResourceNotFoundException(2001));
        assert coordenador != null;

        Curso cursoSalvo = coordenadorService.addCurso(coordenador.getEmail(), "Ciência da Computação", "nivel teste");

        Assertions.assertEquals(coordenador, cursoSalvo.getCoordenador());
        Assertions.assertEquals("Ciência da Computação", cursoSalvo.getNome());
        Assertions.assertEquals("nivel teste", cursoSalvo.getNivel());
    }

    @Test
    @Transactional
    public void deveAssociarDepoimentoAEgresso() {
        Egresso egresso = egressoRepository.findById(2001).orElseThrow(()-> new ResourceNotFoundException(2001));
        assert egresso != null;

        Depoimento depoimento = new Depoimento();

        Depoimento depoimentoSalvo = coordenadorService.lincarDepoimentoEgresso(egresso, depoimento);

        Assertions.assertEquals(depoimento, depoimentoSalvo);
    }

    @Test
    @Transactional
    public void deveVerificarSalvarOCoordenador() {
        Coordenador coordenador = new Coordenador();
        coordenador.setEmail("login teste");
        coordenador.setSenha("mux123");
        coordenador.setTipo("flip");

        Coordenador salvo = coordenadorService.save(coordenador);

        Assertions.assertNotNull(salvo);
        Assertions.assertEquals(coordenador.getEmail(), salvo.getEmail());
        Assertions.assertEquals(coordenador.getSenha(), salvo.getSenha());
        Assertions.assertEquals(coordenador.getTipo(), salvo.getTipo());

    }

    @Test
    @Transactional
    public void deveVerificarAtualizarOCoordenador() {
        Coordenador coordenador = coordenadorRepository.findById(2001).orElseThrow(()-> new ResourceNotFoundException(2001));
        assert coordenador != null;

        coordenador.setEmail("login teste");
        coordenador.setSenha("mux123");
        coordenador.setTipo("flip");

        Coordenador salvo = coordenadorService.save(coordenador);

        Assertions.assertNotNull(salvo);
        Assertions.assertEquals(coordenador.getEmail(), salvo.getEmail());
        Assertions.assertEquals(coordenador.getSenha(), salvo.getSenha());
        Assertions.assertEquals(coordenador.getTipo(), salvo.getTipo());
    }

    @Test
    @Transactional
    public void deveBuscarPorIdExistente() {
        Coordenador coordenador = coordenadorRepository.findById(2001).orElseThrow(()-> new ResourceNotFoundException(2001));
        assert coordenador != null;

        Coordenador encontrado = coordenadorService.findById(coordenador.getId());

        Assertions.assertEquals(coordenador, encontrado);
    }

    @Test
    @Transactional
    public void deveListarTodosOsCoordenadores() {

        List<Coordenador> coordenadores = coordenadorRepository.findAll();
        List<Coordenador> coordenadoresEsperados = entityManager
                .createQuery("SELECT c FROM Coordenador c", Coordenador.class)
                .getResultList();

        Assertions.assertEquals(coordenadores, coordenadoresEsperados);
    }

    @Test
    @Transactional
    public void deveDeletarCoordenador() {
        Coordenador coordenador = coordenadorRepository.findById(2001).orElseThrow(()-> new ResourceNotFoundException(2001));
        assert coordenador != null;

        coordenadorService.delete(coordenador.getId());
        Assertions.assertFalse(coordenadorRepository.existsById(coordenador.getId()));
    }

    @Test
    @Transactional
    public void deveEfetuarLogin() {
        boolean resultado = coordenadorService.efetuarLogin("coordenador1", "mux123");
        Assertions.assertTrue(resultado);
    }

}